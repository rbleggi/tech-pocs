package com.rbleggi.guitarfactory;

public class Guitar {
    private final String guitarType;
    private final String model;
    private final String specs;
    private final String os;

    private Guitar(String guitarType, String model, String specs, String os) {
        this.guitarType = guitarType;
        this.model = model;
        this.specs = specs;
        this.os = os;
    }

    public String getGuitarType() {
        return guitarType;
    }

    public String getModel() {
        return model;
    }

    public String getSpecs() {
        return specs;
    }

    public String getOs() {
        return os;
    }

    @Override
    public String toString() {
        return "Type: " + guitarType + ", Model: " + model + ", Specs: " + specs + ", OS: " + os;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guitar guitar = (Guitar) o;
        return guitarType.equals(guitar.guitarType) &&
               model.equals(guitar.model) &&
               specs.equals(guitar.specs) &&
               os.equals(guitar.os);
    }

    @Override
    public int hashCode() {
        int result = guitarType.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + specs.hashCode();
        result = 31 * result + os.hashCode();
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String guitarType = "";
        private String model = "";
        private String specs = "";
        private String os = "";

        private Builder() {}

        public Builder guitarType(String guitarType) {
            this.guitarType = guitarType;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder specs(String specs) {
            this.specs = specs;
            return this;
        }

        public Builder os(String os) {
            this.os = os;
            return this;
        }

        public Guitar build() {
            return new Guitar(guitarType, model, specs, os);
        }
    }
}
