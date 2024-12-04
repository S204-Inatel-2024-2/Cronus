package com.example.teste;

public class Atividade {
    private String titulo;
    private String descricao;
    private String prioridade;
    int priority;
    private String hora;
    private String data;
    private String activityId; // ID do documento para exclusão

    public Atividade(String titulo, String descricao, String prioridade, String hora, String data, String activityId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.hora = hora;
        this.data = data;
        this.activityId = activityId;
    }

    public Atividade(String titulo, String descricao, String prioridade, String hora, String activityId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.hora = hora;
        this.activityId = activityId;
    }

    public Atividade(String titulo, String activityId)
    {
        this.titulo = titulo;
        this.activityId = activityId;
    }

    public String getActivityId() {
        return activityId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
