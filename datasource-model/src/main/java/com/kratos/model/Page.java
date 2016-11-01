package com.kratos.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private int page = 1;
	@Getter
	@Setter
	private int size = 10;

	private int start = 0;

	@Getter
	private int total = 0;
	@Getter
	private int pages;
	@Getter
	@Setter
	private List<T> datas;

	public Page() {
	}

	public Page(int page, int size) {
		this.page = page;
		this.size = size;
		this.start = page > 0 ? (page - 1) * size : 0;
	}

	public int getStart() {
		return this.page > 0 ? (this.page * this.size - this.size) : 0;
	}

	public void setTotal(int total) {
		this.total = total;
		this.pages = (total / this.size + ((total % this.size == 0) ? 0 : 1));
	}
}
