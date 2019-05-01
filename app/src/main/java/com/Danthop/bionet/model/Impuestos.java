package com.Danthop.bionet.model;

public class Impuestos {
        String ImpuestosID;
        String ValorImpuesto;

        public Impuestos(String impuestosID, String valorImpuesto) {
            ImpuestosID = impuestosID;
            ValorImpuesto = valorImpuesto;
        }

        public String getImpuestosID() {
            return ImpuestosID;
        }

        public void setImpuestosID(String impuestosID) {
            ImpuestosID = impuestosID;
        }

        public String getValorImpuesto() {
            return ValorImpuesto;
        }

        public void setValorImpuesto(String valorImpuesto) {
            ValorImpuesto = valorImpuesto;
        }

}
