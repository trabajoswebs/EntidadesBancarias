/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidadesbancarias;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Johan Manuel
 */
public class EntidadesBancarias {
    
    public static final String DIRECTORY = "..\\Profesores\\";
    public static final String ENTBANCARIASFILEPATH = DIRECTORY + "EntidadesBancarias.txt";
    
    static Scanner sc = new Scanner(System.in);
    
    static TreeMap<String, String> tmEEEE = new TreeMap<String, String>();//Map con entidades bancarias

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        cargaEntidadesBancarias(tmEEEE);
        menuEntidadesBancarias();
    }
    
    
    static void cargaEntidadesBancarias(TreeMap<String, String> tmEEEE){
        cargaDatos(ENTBANCARIASFILEPATH, tmEEEE);
    }
    
    
    /**
     * SUBMENU
     */
    public static void menuEntidadesBancarias(){
        int opcion = 0;
        boolean continuar = true;
        
        do{             
            System.out.println("\n*************** MANTENIMIENTO DE LAS ENTIDADES BANCARIAS ***************\n");
            System.out.println("\t1. ALTA DE ENTIDADES BANCARIAS");
            System.out.println("\t2. BAJA DE ENTIDADES BANCARIAS");
            System.out.println("\t3. MOSTRAR ENTIDADES");            
            System.out.println("\t0. SALIR ENTIDADES");            
            System.out.print("\n\t   Opcion seleccionada: ");
            
            opcion = sc.nextInt();
            
            switch(opcion){
                case 0: default:
                    sc.nextLine(); //Limpiamos el Buffer
                    continuar = false;
                    break;
                case 1:
                    sc.nextLine(); //Limpiamos el Buffer
                    System.out.println("\n1. ALTA DE ENTIDADES BANCARIAS");
                    altaEntidadesBancarias();
                    break;
                case 2:
                    sc.nextLine(); //Limpiamos el Buffer
                    System.out.println("\n2. BAJA DE ENTIDADES BANCARIAS");
                    bajaEntidadesBancarias();
                    break;
                case 3:
                    System.out.println("\n3. MOSTRAR SUCURSALES");
                    System.out.println(imprimeEntidadesBancarias());                   
                    break;
            }
            
        }while(continuar);
        
    }
    
    
    /**
     * Se da de alta a un curso en el fichero entidadessBancarias.txt
     * 
     */
    public static void altaEntidadesBancarias() {
        
        String codEntidadBancaria,  nombreEntidadBancaria, cadena, continuar;        
        RandomAccessFile fichero = null; 
        long size = 0;
        int indice;
        boolean repetir = false;
        
        do {                          
                System.out.println("\nListado de las Entidades bancarias:\n");
                System.out.println(imprimeEntidadesBancarias());

            try {
                System.out.println("Introduzca el código de la Entidad Bancaria:");
                codEntidadBancaria = sc.nextLine();
                
                 if (codEntidadBancaria.isEmpty()) {
                    throw new Exception("Debe introducir el código de la Sucursal Bancaria.");
                }
                
                if (tmEEEE.containsKey(codEntidadBancaria)) { //Comprobamos la existencia del código de la entidad bancaria
                    throw new Exception("La Entidad Bancaria ya existe.");
                }
                
                System.out.println("Introduzca el nombre de la Entidad Bancaria:");
                
                nombreEntidadBancaria = sc.nextLine();                
               
                if (nombreEntidadBancaria.isEmpty()) {
                    throw new Exception("Debe introducir el nombre de la Entidad Bancaria.");
                }

                crearFichero(DIRECTORY, ENTBANCARIASFILEPATH); //Se crea el fichero si no existe
                
                fichero = new RandomAccessFile(ENTBANCARIASFILEPATH, "rw");
                cadena = fichero.readLine();
                
                while(cadena != null){
                    indice = cadena.indexOf(",");
                    if(indice != -1){                        
                        if (cadena.substring(0, indice).equalsIgnoreCase(codEntidadBancaria)) //Obtenemos el código del curso
                            throw new Exception("El código de la Sucursal Bancaria ya se encuentra en el fichero");
                    }
                    cadena = fichero.readLine();                    
                }                
                size = fichero.length(); 
                fichero.seek(size);// nos situamos al final del fichero
                cadena = codEntidadBancaria.toUpperCase() + "," + nombreEntidadBancaria + "\n";
                fichero.writeBytes(cadena);
                
                if (tmEEEE.containsKey(codEntidadBancaria)) { //Actualizamos el TreeMap
                    tmEEEE.put(codEntidadBancaria, "," + nombreEntidadBancaria + "\n"); // añadimos el la sucursal bancaria del treemap
                } else {
                    tmEEEE.clear();//Eliminamos todos los datos del TreeMap
                    cargaEntidadesBancarias(tmEEEE); //volcamos los datos del fichero al TreeMap y lo actualizamos
                }
                System.out.println("Se ha añadido correctamente la Entidad Bancaria en el fichero.");
                System.out.println("Si desea añadir más Entidades Bancarias al fichero introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir =(continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo sucursal bancaria
                
            } catch (FileNotFoundException ex) {     //Si no se encuentra el fichero            
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
                sc.nextLine();
            } catch (IOException ex) {
                repetir = true;
                Logger.getLogger("Ha ocurrido una excepción: " + EntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
                sc.nextLine();
            } catch (Exception ex) {                
                repetir = true;
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
                System.out.println("Si desea añadir más Entidades Bancarias al fichero introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir =(continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo sucursal bancaria
            } finally{
                if (fichero != null) {
                    try {
                        fichero.close();
                    } catch (IOException ex) {
                        Logger.getLogger("Ha ocurrido una excepción: " + EntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        } while (repetir);
        
    }
    
    
    /**
     * Elimina la sucursal bancaria en el fichero Profesores/entidadesBancarias.txt
     */
    
    public static void bajaEntidadesBancarias(){
        
        File ficheroActualizado = new File(DIRECTORY + "entidadesActualizadas.txt");
        String codEntidad, cadena, continuar;
        int indice;
        RandomAccessFile fichero = null;
        RandomAccessFile ficheroNuevo = null;
        boolean repetir = false;
        
       
        do {      
            
            try {
                       
                boolean existeCodigo = false;
                System.out.println("\nListado de las sucursales bancarias:\n");
                System.out.println(imprimeEntidadesBancarias());
                System.out.print("Indique el código de la sucursal que desea eliminar: ");
                codEntidad = sc.nextLine();
                
                if (codEntidad.trim().isEmpty()) {
                    throw new Exception("Debe introducir un código de asignatura válido.");
                }
                
                if (! tmEEEE.containsKey(codEntidad)) { //Comprobamos la existencia del código de la sucursal bancaria
                    throw new Exception("La sucursal no existe.");
                }
                
                fichero = new RandomAccessFile(ENTBANCARIASFILEPATH, "r");
                
                if(fichero.length() == 0) throw new Exception("El fichero de las sucursales se encuentra vacio.");
                
                cadena = fichero.readLine();
                
                while(cadena != null){
                    indice = cadena.indexOf(",");
                    if (indice != -1) {
                        if(cadena.substring(0, indice).equalsIgnoreCase(codEntidad)){
                            existeCodigo = true;
                            break; // dejamos de recorrer el fichero saliendo del while
                        } 
                            
                    }                    
                    cadena = fichero.readLine();
                }
                
                if (! existeCodigo) // Si el código no existe en el fichero lanzamos la exepción
                        throw new Exception("El código de la Entidad Bancaria (" + codEntidad.toUpperCase() + ") que se desea eliminar no existe en el fichero.");
                
                crearFichero(DIRECTORY, "entidadesActualizadas.txt"); //creamos un nuevo fichero
                
                ficheroNuevo = new RandomAccessFile(ficheroActualizado, "rw");
                
                fichero.seek(0); //Llevamos el puntero al inicio
                
                 cadena = fichero.readLine();
               
               while (cadena != null) {
                    indice = cadena.indexOf(",");
                    if(indice != -1){  
                        if (! cadena.substring(0, indice).equalsIgnoreCase(codEntidad)) {
                            
                            ficheroNuevo.seek(ficheroNuevo.getFilePointer());
                            ficheroNuevo.writeBytes(cadena + "\n");
                        }
                    }
                    cadena = fichero.readLine();
                }
             
                ficheroNuevo.close(); //Cerramos el fichero actualizado
                fichero.close(); //Debemos cerrar el fichero antes de eliminarlo
                
                File ficheroOriginal = new File(DIRECTORY + "entidadesActualizadas.txt");
                File destFichero = new File(ENTBANCARIASFILEPATH);
                
                 if (destFichero.delete()) { //Borramos el fichero anterior
                     
                        if (ficheroOriginal.renameTo(destFichero)) {//Renombramos el fichero
                            
                            if (tmEEEE.containsKey(codEntidad)) { //Actualizamos el TreeMap
                                tmEEEE.remove(codEntidad); // Eliminamos la sucursal bancaria del treemap
                            }else{
                                tmEEEE.clear();//Eliminamos todos los datos del TreeMap
                                cargaEntidadesBancarias(tmEEEE); //Si ocurre un error volcamos los datos del fichero al TreeMap y lo actualizamos
                            }

                        System.out.println("Se ha eliminado correctamente la entidad bancaria  " + codEntidad + " del fichero.");
                        System.out.println("Si desea eliminar más entidades bancarias de la lista introduzca la letra: \"S\"");

                        continuar = sc.nextLine();
                        repetir = (continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo cursos

                    } else {
                        throw new Exception("No se ha podido renombrar el archivo " + ficheroActualizado.getName() + " a " + ENTBANCARIASFILEPATH); 
                    }

                } else {
                    throw new Exception("No se ha podido eliminar el fichero " + destFichero.getName() + " necesario para reeditarlo.");
                }
                

            } catch (FileNotFoundException ex) {//Si no se encuentra el fichero            
                System.out.println("Ha ocurrido una excepción: " + ex.getMessage());
            }catch(IOException ieo){
                System.out.println("Se ha producido un error: " + ieo.getMessage());
                sc.nextLine();
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
                System.out.println("Si desea eliminar alguna entidad bancaria de la lista introduzca la letra: \"S\"");
                continuar = sc.nextLine();
                repetir = (continuar.equalsIgnoreCase("S")); //Si se desea continuar añadiendo sucursales bancarias
                
            } finally {
                try {
                    if (fichero != null) {
                        fichero.close();
                    }
                    if (ficheroNuevo != null) {
                        ficheroNuevo.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }                
            }

        } while (repetir);
        
    }
    
    /**
     * Imprime por pantalla Sucursales bancarias que se encuentran en el fichero
     * @return String codigo y nombre de las sucursales bancarias
     */
    
    public static String imprimeEntidadesBancarias() {
        String cadena;
        FileReader fr = null;
        BufferedReader entrada = null;
        StringBuilder cursos = new StringBuilder();
        
        try {            
            fr = new FileReader(ENTBANCARIASFILEPATH);
            entrada = new BufferedReader(fr);
            cadena = entrada.readLine();
            while(cadena != null){
                cursos.append(cadena);
                cursos.append("\n");
                cadena = entrada.readLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(EntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return cursos.toString();
    }
    
    /**
     * Lee los datos que se encuentra en el fichero y los carga en el treeMap
     */
    public static void cargaDatos(String filePath, TreeMap<String, String> tm){
        FileReader fr = null;
        BufferedReader entrada = null;
        int indice = 0;
        String cadena, key, value;
        
        try {
            fr = new FileReader(filePath);
            entrada = new BufferedReader(fr);
            
            cadena = entrada.readLine();
            
            while(cadena != null){
                indice = cadena.indexOf(",");
                if (indice != -1) {
                    key = cadena.substring(0, indice).toUpperCase();
                    value = cadena.substring(indice + 1 ).toUpperCase();
                    tm.put(key, value);
                }
                cadena = entrada.readLine();
            }
        }
        catch (FileNotFoundException fnf) {
            fnf.printStackTrace();
        }
        catch (IOException ioe) {
            System.out.println("Ha ocurrido una excepción: " + ioe.getMessage());    
        }
        catch (Exception e) {
            System.out.println("Ha ocurrido una excepción: " + e.getMessage());   
        }finally{
            try {
                if (fr != null) {
                    fr.close();
                }
                if(entrada != null){
                    entrada.close();
                }
            } catch (IOException e) {
                System.out.println("Se ha producido un error al intentar cerrar el fichero: " + e.getMessage());
            }
        }
    }
    
    /**
     * Crea el fichero, pasado por parametro, si no existe
     * @param ruta direccion de la ubicacion del fichero
     * @param fichero nombre del fichero
     * @throws IOException 
     */
    public static void crearFichero(String ruta, String fichero) throws IOException {
        File cursoRuta = new File(ruta);
        File cursoFichero = new File(cursoRuta, fichero);
        
        if (! cursoFichero.exists()) {

            System.out.println("El fichero " + cursoFichero.getAbsolutePath() + " no existe.");

            if (!cursoRuta.exists()) {
                System.out.println("El directorio " + cursoRuta.getAbsolutePath() + " no existe.");

                if (cursoRuta.mkdir()) {
                    System.out.println("Se ha creado el directorio " + cursoRuta.getAbsolutePath());

                    if (cursoFichero.createNewFile()) {
                        System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                    } else {
                        throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                    }

                } else {
                    throw new IOException("No se ha podido crear la ruta " + cursoRuta.getAbsolutePath());
                }
            } else {

                if (cursoFichero.createNewFile()) {
                    System.out.println("Se ha creado el fichero " + cursoFichero.getName());
                } else {
                    throw new IOException("No se ha podido crear el fichero " + cursoFichero.getName());
                }

            }
        }
    }
    
}
