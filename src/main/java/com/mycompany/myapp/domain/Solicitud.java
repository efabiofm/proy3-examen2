package com.mycompany.myapp.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Solicitud.
 */
@Entity
@Table(name = "solicitud")
public class Solicitud implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_entrega")
    private ZonedDateTime fechaEntrega;

    @Column(name = "es_recursivo")
    private Boolean esRecursivo;

    @ManyToOne
    private Frecuencia frecuencia;

    @ManyToOne
    private Estado estado;

    @ManyToMany
    @JoinTable(name = "solicitud_tipo",
               joinColumns = @JoinColumn(name="solicituds_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="tipos_id", referencedColumnName="id"))
    private Set<Tipo> tipos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public Solicitud fechaEntrega(ZonedDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
        return this;
    }

    public void setFechaEntrega(ZonedDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Boolean isEsRecursivo() {
        return esRecursivo;
    }

    public Solicitud esRecursivo(Boolean esRecursivo) {
        this.esRecursivo = esRecursivo;
        return this;
    }

    public void setEsRecursivo(Boolean esRecursivo) {
        this.esRecursivo = esRecursivo;
    }

    public Frecuencia getFrecuencia() {
        return frecuencia;
    }

    public Solicitud frecuencia(Frecuencia frecuencia) {
        this.frecuencia = frecuencia;
        return this;
    }

    public void setFrecuencia(Frecuencia frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Estado getEstado() {
        return estado;
    }

    public Solicitud estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Set<Tipo> getTipos() {
        return tipos;
    }

    public Solicitud tipos(Set<Tipo> tipos) {
        this.tipos = tipos;
        return this;
    }

    public Solicitud addTipo(Tipo tipo) {
        this.tipos.add(tipo);
        return this;
    }

    public Solicitud removeTipo(Tipo tipo) {
        this.tipos.remove(tipo);
        return this;
    }

    public void setTipos(Set<Tipo> tipos) {
        this.tipos = tipos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Solicitud solicitud = (Solicitud) o;
        if (solicitud.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, solicitud.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Solicitud{" +
            "id=" + id +
            ", fechaEntrega='" + fechaEntrega + "'" +
            ", esRecursivo='" + esRecursivo + "'" +
            '}';
    }
}
