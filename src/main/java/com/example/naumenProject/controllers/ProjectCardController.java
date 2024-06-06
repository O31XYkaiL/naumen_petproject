package com.example.naumenProject.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@Api("Контроллер для фронта админ. части")
@Controller
//@RequestMapping("/")
public class ProjectCardController {
//    @Autowired
//    private AnalyzeDBUtils analyzeDBUtils;
//
//    @GetMapping({"/"})
//    public String getInfoDatabase(Model model) {
//        model.addAttribute("infoTables", analyzeDBUtils.getInfoTables().values());
//
//        return "info_database";
//    }
//
//    @GetMapping({"/info-table/{tableName}"})
//    public String getInfoTable(Model model, @PathVariable String tableName) {
//        model.addAttribute("infoFields", analyzeDBUtils.getInfoTableFields(tableName));
//        model.addAttribute("tableName", tableName);
//        model.addAttribute("columnNames", analyzeDBUtils.getInfoTableFields(tableName)
//                .stream().map(field -> String.format("Название колонки: '%s' (тип данных: %s)", field.getColumnName(), field.getTypeField().toString()))
//                .toList().toString()
//        );
//
//        return "info_table";
//    }
}
