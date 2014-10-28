/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cp;

import java.io.File;

/**
 *
 * @author Douglas
 */
public class GenerateXSD {

    public static void main(String[] args) {
        String input = GenerateXSD.class.getClassLoader().getResource("br/edu/utfpr/cp/MetricResults.xml").getPath();
        System.out.println(input);     
        String output = new File("xml-resources/jaxb/BindMetricsCodePro/Metrics.xsd").getAbsolutePath();
        System.out.println(output);
        com.thaiopensource.relaxng.translate.Driver.main(new String[]{"-I", "xml", "-O", "xsd", input, output});
    }
}
