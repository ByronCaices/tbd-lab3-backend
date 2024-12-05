package tbd.lab1.models;

public class UsuarioActivo {
    private Integer id_registro;
    private long total_queries;

    // Constructor sin argumentos
    public UsuarioActivo() {
    }
    // Constructor
    public UsuarioActivo(Integer id_registro, long totalQueries) {
        this.id_registro = id_registro;
        this.total_queries = totalQueries;
    }

    // Getters y Setters
    public Integer getId_registro() {
        return id_registro;
    }

    public void setId_registro(Integer id_registro) {
        this.id_registro = id_registro;
    }

    public long getTotal_queries() {
        return total_queries;
    }

    public void setTotal_queries(long total_queries) {
        this.total_queries = total_queries;
    }
}
