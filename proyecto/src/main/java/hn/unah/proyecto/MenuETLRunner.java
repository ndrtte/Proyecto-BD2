package hn.unah.proyecto;

import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import hn.unah.proyecto.dto.SourceTableDTO;
import hn.unah.proyecto.servicios.MenuETLService;

@Component
public class MenuETLRunner implements CommandLineRunner {

    private Scanner sc = new Scanner(System.in);

    private String sourceTable;
    private String method;

    @Autowired
    private MenuETLService menuETLService;



    @Override
    public void run(String... args) {

        System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.println("**********************************************************");
        System.out.println("Iniciando el proceso ETL \n\n");
        System.out.println("Bienvenido a la migracion de datos para la base de datos de Sakila");
        System.out.println("\n\n\n\n");
        System.out.println("Elige el metodo por el que quieres seleccionar la tabla origen:\n");

        System.out.println("(a) Por nombre de tabla ");
        System.out.println("(b) Por consula SQL");

        do{
            System.out.print("Selecciona una opcion valida: ");
            method = sc.nextLine();
        }
        while(!method.equals("a") && !method.equals("b"));

        if(method.equals("a")){
                selectSourceTable();

        }else if(method.equals("b")){
                querySourceTable();
        }

    }

    private void selectSourceTable() {
        System.out.println("----------------------------------");
        System.out.println("\n\n");
        System.out.print("Ingresa el nombre de la tabla origen: ");
        sourceTable = sc.nextLine();

        SourceTableDTO sourceTableDTO = new SourceTableDTO();
        sourceTableDTO.setSourceTable(sourceTable);
        sourceTableDTO.setMethod("Tabla");

        List <String> columnas = menuETLService.obtenerColumnas(sourceTableDTO);
        int i = 0;
        for (String columna : columnas) {
            i++;
            System.out.println("("+i+")" + columna);
        }




        
    }
    

    private void querySourceTable() {
        System.out.println("----------------------------------");
        System.out.println("\n\n");
        System.out.print("Ingresa la consulta SQL: ");
        sourceTable = sc.nextLine();
    }
}


