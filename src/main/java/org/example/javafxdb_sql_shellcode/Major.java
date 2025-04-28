package org.example.javafxdb_sql_shellcode;

public enum Major {
    CS("Computer Science"),
    CPIS("Computer Information Systems"),
    ENGLISH("English"),
    MATH("Mathematics"),
    BIOLOGY("Biology"),
    CHEMISTRY("Chemistry");

    private final String displayName;

    Major(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public String getValue() {
        return name();
    }
}