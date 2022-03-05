package com.tfip2021.module4.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SupportedProvider {
   private String provider;
   /*
   * Suggested location of the provider's image served from the backend.
   * May be overriden by frontend.
   */
   private String imageUri;
   private String imageAlt;
   private String loginUri;
   private String signUpUri;
}
