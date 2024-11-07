package com.desafiotecnico.ponta.cesarlopes.Tarefas;

public enum Status {
    PENDENTE("pendente"),
    EM_ANDAMENTO("em andamento"),
    CONCLUIDA("concluída");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static boolean statusEhValido(String status) {
        for (Status s : Status.values()) {
            if (s.value.equals(status)) {
                return true;
            }
        }
        return false;
    }
}
