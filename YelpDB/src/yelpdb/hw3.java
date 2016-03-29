/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yelpdb;

/**
 *
 * @author Tim
 */
public class hw3 {
    public static void main(String[] args) {
        try {
            populate.tableSetup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
