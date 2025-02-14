package com.ramon.tarea3AD2024base.data;

import org.springframework.stereotype.Component;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

@Component
public class DataConnection {

       private static DataConnection INSTANCE = null;
       private final String PATH = "ServiciosContratados.db4o";
       private static ObjectContainer db;

       // Private constructor suppresses
       private DataConnection() {
       }

       // Creador sincronizado para protegerse de posibles problemas multi-hilo
       // Otra prueba para evitar instanciaci�n m�ltiple
       private synchronized static void createInstance() {
	   		if (INSTANCE == null) { 
	   			INSTANCE = new DataConnection();
	   			INSTANCE.performConnection();
	   		}
   		}

       public static ObjectContainer getInstance() {
            if (INSTANCE == null)
                createInstance();
            return db;
       }

       public void performConnection() {
	   		//EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
	   		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), PATH);
   	}
       
       public void closeConnection() {
           db.close();
       }

}