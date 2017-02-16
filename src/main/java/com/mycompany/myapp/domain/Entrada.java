package com.mycompany.myapp.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Entrada.
 */
@Entity
@Table(name = "entrada")
public class Entrada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kilos")
    private Double kilos;

    @Column(name = "fecha_entrada")
    private ZonedDateTime fechaEntrada;

    @ManyToOne
    private Beneficio beneficio;

    @ManyToOne
    private Tipo tipo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getKilos() {
        return kilos;
    }

    public Entrada kilos(Double kilos) {
        this.kilos = kilos;
        return this;
    }

    public void setKilos(Double kilos) {
        this.kilos = kilos;
    }

    public ZonedDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public Entrada fechaEntrada(ZonedDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
        return this;
    }

    public void setFechaEntrada(ZonedDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public Beneficio getBeneficio() {
        return beneficio;
    }

    public Entrada beneficio(Beneficio beneficio) {
        this.beneficio = beneficio;
        return this;
    }

    public void setBeneficio(Beneficio beneficio) {
        this.beneficio = beneficio;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Entrada tipo(Tipo tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Entrada entrada = (Entrada) o;
        if (entrada.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, entrada.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Entrada{" +
            "id=" + id +
            ", kilos='" + kilos + "'" +
            ", fechaEntrada='" + fechaEntrada + "'" +
            '}';
    }
}
