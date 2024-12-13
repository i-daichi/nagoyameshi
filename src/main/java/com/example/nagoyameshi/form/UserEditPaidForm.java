package com.example.nagoyameshi.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserEditPaidForm {
	private Integer id;

    private String stripeToken;

    // GetterとSetter
    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }
}
