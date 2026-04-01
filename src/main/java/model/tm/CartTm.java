package model.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartTm {
    private String code;
    private String name;
    private double unitPrice;
    private int qty;
    private double total;
    private Button btnRemove;
}