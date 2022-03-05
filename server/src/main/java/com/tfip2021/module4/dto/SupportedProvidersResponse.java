package com.tfip2021.module4.dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SupportedProvidersResponse {
    private SupportedProvider local;
    private List<SupportedProvider> social;
}
