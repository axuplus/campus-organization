package com.safe.campus.about.exception;

import com.safe.campus.enums.ResultCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CampusException extends RuntimeException {
    private ResultCodeEnum resultCodeEnum;
}
