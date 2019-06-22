package dev.posadskiy.skillrepeat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestException {
	private String title;
	private int code;
	private String message;
}
