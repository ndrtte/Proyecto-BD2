let selectedMethod = null;
let selectedTable = null;
let selectedColumns = [];
let availableColumns = [];
let selectedDestinationTable = null;

// Manejo de selección de método
document.querySelectorAll('.method-btn').forEach(btn => {
    btn.addEventListener('click', function () {
        // Remover selección anterior
        document.querySelectorAll('.method-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.input-section').forEach(s => s.classList.remove('active'));

        // Activar nueva selección
        this.classList.add('active');
        selectedMethod = this.dataset.method;

        if (selectedMethod === 'table') {
            document.getElementById('table-section').classList.add('active');
        } else {
            document.getElementById('query-section').classList.add('active');
        }

        // Limpiar secciones anteriores
        clearResults();
    });
});

// Cargar columnas al hacer clic en el botón
document.getElementById('load-columns-btn').addEventListener('click', async function () {
    const tableNameInput = document.getElementById('table-name');
    const tableName = tableNameInput.value.trim().toUpperCase();

    if (!tableName) {
        showError('Por favor ingresa el nombre de una tabla');
        return;
    }

    selectedTable = tableName;
    await loadColumns(tableName);
});

// Permitir cargar columnas con Enter
document.getElementById('table-name').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        document.getElementById('load-columns-btn').click();
    }
});

// Cargar columnas de la tabla
async function loadColumns(tableName) {
    const loadBtn = document.getElementById('load-columns-btn');
    const columnsSection = document.getElementById('columns-section');
    const columnsGrid = document.getElementById('columns-grid');

    loadBtn.disabled = true;
    loadBtn.textContent = 'Cargando...';

    try {
        const response = await fetch('http://localhost:8080/api/etl/obtener/columnas', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: `sourceTable=${encodeURIComponent(tableName)}`
        });

        if (response.ok) {
            const columns = await response.json();

            if (columns && columns.length > 0) {
                availableColumns = columns;
                selectedColumns = [];
                columnsGrid.innerHTML = '';

                columns.forEach((column, index) => {
                    const columnDiv = document.createElement('div');
                    columnDiv.className = 'column-item';
                    columnDiv.innerHTML = `
                                <input type="checkbox" id="col-${index}" value="${column}">
                                <label for="col-${index}">${column}</label>
                            `;

                    const checkbox = columnDiv.querySelector('input');
                    checkbox.addEventListener('change', function () {
                        if (this.checked) {
                            selectedColumns.push(this.value);
                            columnDiv.classList.add('selected');
                        } else {
                            selectedColumns = selectedColumns.filter(col => col !== this.value);
                            columnDiv.classList.remove('selected');
                        }
                    });

                    columnsGrid.appendChild(columnDiv);
                });

                columnsSection.classList.add('active');
                showSuccess(`Se encontraron ${columns.length} columnas en la tabla ${tableName}`);
            } else {
                showError(`La tabla ${tableName} no existe o no tiene columnas.`);
            }
        } else {
            const errorText = await response.text();
            showError(`Error al cargar columnas: ${errorText}`);
        }
    } catch (error) {
        console.error('Error:', error);
        showError('Error de conexión al servidor');
    } finally {
        loadBtn.disabled = false;
        loadBtn.textContent = 'Cargar Columnas';
    }
}

// Seleccionar todas las columnas
document.getElementById('select-all-btn').addEventListener('click', function () {
    const checkboxes = document.querySelectorAll('#columns-grid input[type="checkbox"]');
    const allSelected = selectedColumns.length === availableColumns.length;

    checkboxes.forEach((checkbox, index) => {
        checkbox.checked = !allSelected;
        const columnDiv = checkbox.closest('.column-item');

        if (!allSelected) {
            columnDiv.classList.add('selected');
            if (!selectedColumns.includes(checkbox.value)) {
                selectedColumns.push(checkbox.value);
            }
        } else {
            columnDiv.classList.remove('selected');
        }
    });

    if (allSelected) {
        selectedColumns = [];
    } else {
        selectedColumns = [...availableColumns];
    }

    this.textContent = allSelected ? 'Seleccionar Todas' : 'Deseleccionar Todas';
});

// Manejo del botón Run ETL
document.getElementById('run-etl').addEventListener('click', async function () {
    if (!selectedMethod) {
        showError('Por favor selecciona un método primero');
        return;
    }

    if (selectedMethod === 'table') {

        selectedDestinationTable = document.getElementById('destination-table-table')?.value.trim().toUpperCase();

        if (!selectedTable) {
            showError('Por favor ingresa el nombre de una tabla y carga sus columnas');
            return;
        }

        if (selectedColumns.length === 0) {
            showError('Por favor selecciona al menos una columna');
            return;
        }

        if (!selectedDestinationTable) {
            showError('Por favor ingresa el nombre de la tabla destino');
            return;
        }

        if (!selectedDestinationTable) {
            showError('Por favor ingresa el nombre de la tabla destino');
            return;
        }

        await runETLTable();

    } else {
        selectedDestinationTable = document.getElementById('destination-table-query')?.value.trim().toUpperCase();
        const sqlQuery = document.getElementById('sql-query').value.trim();
        if (!sqlQuery) {
            showError('Por favor ingresa una consulta SQL');
            return;
        }

        if (!selectedDestinationTable) {
            showError('Por favor ingresa el nombre de la tabla destino');
            return;
        }
        await runETLQuery(sqlQuery);
    }
});

// Ejecutar ETL para tabla
async function runETLTable() {
    const button = document.getElementById('run-etl');
    const progressSection = document.getElementById('progress-section');
    const statusMessages = document.getElementById('status-messages');

    // Preparar UI
    button.classList.add('loading');
    button.disabled = true;
    document.getElementById('btn-text').textContent = 'Ejecutando ETL...';

    progressSection.classList.add('active');
    statusMessages.innerHTML = '';

    addStatusMessage(`Iniciando migración para tabla: ${selectedTable}`);
    addStatusMessage(`Columnas seleccionadas: ${selectedColumns.join(', ')}`);

    const migrationData = {
        sourceTable: selectedTable,
        destinationTable: selectedDestinationTable,
        listColumn: selectedColumns,
        method: selectedMethod
    };

    try {
        const response = await fetch('http://localhost:8080/api/etl/migrar/datos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(migrationData)
        });

        if (response.ok) {
            const result = await response.text();
            addStatusMessage('Migración completada exitosamente', 'success');
            if (result && result.trim()) {
                addStatusMessage(`Resultado: ${result}`, 'success');
            }
        } else {
            const errorText = await response.text();
            addStatusMessage(`Error en la migración: ${errorText}`, 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        addStatusMessage('Error de conexión al servidor', 'error');
    } finally {
        // Restaurar botón
        button.classList.remove('loading');
        button.disabled = false;
        document.getElementById('btn-text').textContent = 'Run ETL';
    }
}

// Ejecutar ETL para consulta SQL
async function runETLQuery(sqlQuery) {
    const button = document.getElementById('run-etl');
    const progressSection = document.getElementById('progress-section');
    const statusMessages = document.getElementById('status-messages');

    // Preparar UI
    button.classList.add('loading');
    button.disabled = true;
    document.getElementById('btn-text').textContent = 'Ejecutando ETL...';

    progressSection.classList.add('active');
    statusMessages.innerHTML = '';

    addStatusMessage(`Ejecutando consulta SQL:`);
    addStatusMessage(sqlQuery, 'warning');

    // Para consultas SQL, se necesita implementar un endpoint diferente
    // Por ahora se muestra la funcionalidad disponible
    try {
        // Aquí se debería hacer la llamada al endpoint correspondiente

        addStatusMessage('Esta funcionalidad aún no está implementada', 'error');
    } catch (error) {
        console.error('Error:', error);
        addStatusMessage('Error de conexión al servidor', 'error');

    } finally {
        // Restaurar botón
        button.classList.remove('loading');
        button.disabled = false;
        document.getElementById('btn-text').textContent = 'Run ETL';
    }
}

// Funciones auxiliares
function addStatusMessage(message, type = 'info') {
    const statusMessages = document.getElementById('status-messages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `status-message ${type}`;
    messageDiv.textContent = `${new Date().toLocaleTimeString()} - ${message}`;
    statusMessages.appendChild(messageDiv);
    statusMessages.scrollTop = statusMessages.scrollHeight;
}

function showError(message) {
    const existingError = document.querySelector('.error-message');
    if (existingError) {
        existingError.remove();
    }

    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;
    document.querySelector('.container').appendChild(errorDiv);

    setTimeout(() => {
        errorDiv.remove();
    }, 5000);
}

function showSuccess(message) {
    const existingSuccess = document.querySelector('.success-message');
    if (existingSuccess) {
        existingSuccess.remove();
    }

    const successDiv = document.createElement('div');
    successDiv.className = 'success-message';
    successDiv.textContent = message;
    document.querySelector('.container').appendChild(successDiv);

    setTimeout(() => {
        successDiv.remove();
    }, 5000);
}

function clearResults() {
    document.getElementById('progress-section').classList.remove('active');
    document.getElementById('columns-section').classList.remove('active');
    document.getElementById('table-name').value = '';
    document.getElementById('sql-query').value = '';
    selectedTable = null;
    selectedColumns = [];
    availableColumns = [];

    const errorMessages = document.querySelectorAll('.error-message, .success-message');
    errorMessages.forEach(msg => msg.remove());
}