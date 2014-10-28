/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.cp;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author Douglas
 */
public class MetricasCodePro {

    private static final String DIT = "com.instantiations.assist.eclipse.averageDepthOfInheritanceHierarchyMetric";

    public static void main(String[] args) {
        try {
            new MetricasCodePro().analyse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Map<String, Resource> resources;

    private void analyse() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);

        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        MetricsResultSet metricsResultSet
                = (MetricsResultSet) unmarshaller.unmarshal(
                        ClassLoader.getSystemResourceAsStream("br/edu/utfpr/cp/MetricResults.xml"));

        //System.out.println(metricsResultSet.getMetricResult().size());
        analyseResourceTypes(metricsResultSet.getAnalysis());

        for (MetricResult result : metricsResultSet.getMetricResult()) {
            if (DIT.equals(result.getMetricId())) {
                System.out.println("class;maximumDIT;minimumDIT;fileType");
                printResultDIT(result, metricsResultSet, "");
            }
        }

    }

    private void printResultDIT(MetricResult result, MetricsResultSet resultSet, String packagePrefix) {
        //  String tab = printTab(1);
        StringBuffer buf = new StringBuffer();
        //  System.out.println(tab + result.getMetricId() + " - " + result.getScope());
        String fileName = (!packagePrefix.isEmpty() ? (packagePrefix + "/") : "") + result.getScope();
        Resource resource = resources.get(fileName);
        buf.append(fileName).append(";");
        for (Object obj : result.getMetricResultOrBreakdownDetailOrComputer()) {
            //   System.out.println(tab + "> " + obj.getClass().getSimpleName());
            if (obj instanceof BreakdownDetail) {
                BreakdownDetail detail = (BreakdownDetail) obj;
                //   System.out.println(tab + tab + detail.getName());
                for (BreakdownLine line : detail.getBreakdownLine()) {
                    //      System.out.println(tab + tab + tab + line.getName() + "=" + line.getValue());
                    buf.append(line.getValue()).append(";");
                }
            } else if (obj instanceof MetricResult) {
                MetricResult r = (MetricResult) obj;
                //System.out.println(fileName);

                printResultDIT(r, resultSet, fileName);

            }
        }
        if (resource != null) {
            buf.append(resource.getType().intValue()).append(";");
            System.out.println(buf);
        } else {
            buf.append(-1).append(";");
            System.err.println(buf);
        }
    }

    private String printTab(int nivel) {
        String tab = "";
        for (int i = 0; i < nivel; i++) {
            tab += "  ";
        }
        return tab;
    }

    private void analyseResourceTypes(Analysis analysis) {
        resources = new HashMap<>();
        analysis.getResource().stream().forEach((res) -> {
            resources.put(res.getPath(), res);
        });
    }

}
