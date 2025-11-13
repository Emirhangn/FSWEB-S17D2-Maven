package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.model.JuniorDeveloper;
import com.workintech.s17d2.model.MidDeveloper;
import com.workintech.s17d2.model.SeniorDeveloper;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/developers")
public class DeveloperController {
 public Map<Integer, Developer> developers;
 private Taxable developerTax;

 @Autowired
 public DeveloperController(Taxable developerTax){
  this.developerTax = developerTax;
 }

 @PostConstruct
 public void init(){
  developers = new HashMap<>();
 }

 @GetMapping
 public List<Developer> getDevelopers(){
  return developers.values().stream().toList();
 }

 @GetMapping("/id")
 public Developer getDeveloper(@PathVariable int id){
  return developers.get(id);
 }
 @PutMapping("/id")
 public void updateDeveloper (@PathVariable Integer id, @RequestBody Developer developer) {
  if (developers.containsKey(id)) {
   developers.put(developer.getId(), developer);
  } else {
   System.out.println("Böyle bir developer bulunamadı");
  }
 }

 @PostMapping
 public Developer insertDeveloper(@RequestBody Developer developer){

  double taxRate = 0;
  Developer newDeveloper = null;

  switch (developer.getExperience()){
   case JUNIOR :
     taxRate = developerTax.getSimpleTaxRate();
     newDeveloper = new JuniorDeveloper(developer.getId(), developer.getName(), developer.getSalary());
   break;
   case MID:
    taxRate = developerTax.getMiddleTaxRate();
    newDeveloper = new MidDeveloper(developer.getId(), developer.getName(), developer.getSalary());
   break;
   case SENIOR:
    taxRate = developerTax.getUpperTaxRate();
    newDeveloper = new SeniorDeveloper(developer.getId(), developer.getName(), developer.getSalary());
   break;
   default:
    System.out.println("Hatalı giriş");
    return null;
  }
  double newSalary = developer.getSalary() - (developer.getSalary()*taxRate);
  newDeveloper.setSalary(newSalary);

  return newDeveloper;
}

 @DeleteMapping("/id")
 public void deleteDeveloper(@PathVariable int id){
  if (developers.containsKey(id)) {
   developers.remove(id);
  } else {
   System.out.println("Böyle bir developer bulunamadı");
  }
 }

}
