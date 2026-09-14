package com.OnlineCanteen.MenuService.controller;

import com.OnlineCanteen.MenuService.entity.Menu_Item;
import com.OnlineCanteen.MenuService.repositrory.MenuRepository;
import com.OnlineCanteen.MenuService.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    @Autowired
    private MenuService service;
    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("/check")
    public String check(){
        return "Ok";
    }
//    @PostMapping
//    public Menu_Item add(@RequestBody Menu_Item menu) {
//        return service.addItem(menu);
//    }
// Newly Added - Accept multipart form data
@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public Menu_Item add(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("price") double price, @RequestParam("prepTime") int prepTime, @RequestParam("available") boolean available, @RequestParam("readyMade") boolean readyMade,
                     @RequestParam("description") String description, @RequestParam("category") String category) throws IOException {

    Menu_Item menu = new Menu_Item();
    menu.setName(name);
    menu.setPrice(price);
    menu.setPrepTime(prepTime);
    menu.setAvailable(available);
    menu.setReadyMade(readyMade);
    menu.setDescription(description);
    menu.setCategory(category);
    return service.addItem(menu, file);
}


    // Done Changes in this api before it looks like @GetMapping
    @GetMapping("/all")
    public List<Menu_Item> getAll() {
        return service.getAllItems();
    }
    @GetMapping("/{id}")
    public Menu_Item getById(@PathVariable Long id) {
        return menuRepository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Menu_Item update(@PathVariable Long id, @RequestBody Menu_Item menu) {
        return service.updateItem(id, menu);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteItem(id);
        return "Deleted successfully";
    }

}
