package com.mballem.curso.security.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "medicos")
public class Medico extends AbstractEntity {

	@Column(name = "nome", unique = true, nullable = false)
	private String nome;
	
	@Column(name = "crm", unique = true, nullable = false)
	private Integer crm;
	
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_inscricao", nullable = false)
	private LocalDate dtInscricao;
	
	// avoid recursion when the response json is created for the datatables.
	// this represents the other side of the many2many relationship between doctors and specialties
	// "set" is being used instead of "list" to inhibit problems with repeated specialties
	@JsonIgnore
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "medicos_tem_especialidades",
			joinColumns = @JoinColumn(name = "id_medico", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "id_especialidade", referencedColumnName = "id")
    )
	private Set<Especialidade> especialidades;
	
	// avoids recursion when the response json is created for the datatables.
	@JsonIgnore
	@OneToMany(mappedBy = "medico")
	private List<Agendamento> agendamentos;
	
	// here @OneToOne means that the doctor has a user, instead of being a user. 
	// difference is dependency with inheritance, in this case it is a dependency
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;
	
	public Medico() {
		super();
	}

	public Medico(Long id) {
		super.setId(id);
	}

	public Medico(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getCrm() {
		return crm;
	}

	public void setCrm(Integer crm) {
		this.crm = crm;
	}

	public LocalDate getDtInscricao() {
		return dtInscricao;
	}

	public void setDtInscricao(LocalDate dtInscricao) {
		this.dtInscricao = dtInscricao;
	}

	public Set<Especialidade> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(Set<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}

	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}

	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}	

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
