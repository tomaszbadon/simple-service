package net.beans.java.example.microservice.simple.data.model.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriesInfo {

    List<CategoryInfo> categories = new ArrayList<>();

}
