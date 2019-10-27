package com.lpan.demo.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.alibaba.fastjson.JSON;

@Entity
@Table(name = "d_address")
public class Address implements Serializable{
	
	private static final long serialVersionUID = 1988827941043533061L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Integer addressId;
	
	private String addressName;
	
	private String city;
	
	/*
	 * 一对多，
	 * cascade = CascadeType.ALL 级联操作
	 * mappedBy = "address" 一对多关联
	 */
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "address")
	private Set<Person> persons = new HashSet<Person>();

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Set<Person> getPersons() {
		return persons;
	}

	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	
	

}
