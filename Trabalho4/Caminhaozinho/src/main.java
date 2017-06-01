/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author decker
 */
import Server.Server;

public class main {

    public static void main(String[] args) {
        
        try {
            String a[] = {"s"};
            remoteDriver.RemoteDriver.main(args);
            remoteDriver.RemoteDriver.main(a);
        } catch (Exception e) {
            
            System.err.println(e);
        }

    }
}
