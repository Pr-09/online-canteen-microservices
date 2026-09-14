package com.OnlineCanteen.MenuService.service;

import com.OnlineCanteen.MenuService.entity.Menu_Item;
import com.OnlineCanteen.MenuService.repositrory.MenuRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository repo;

    @Autowired
    private CloudinaryService cloudinaryService;

//    public Menu_Item addItem(Menu_Item menu) {
//        return repo.save(menu);
//    }

    @Transactional
    public Menu_Item addItem(Menu_Item menu, MultipartFile file) throws IOException {
        // Upload image to Cloudinary
        String imageUrl = cloudinaryService.uploadImage(file);
        menu.setImageUrl(imageUrl);
        return repo.save(menu);
    }

    public List<Menu_Item> getAllItems() {
        return repo.findAll();
    }

    public Menu_Item updateItem(Long id, Menu_Item menu) {
        Menu_Item existing = repo.findById(id).orElseThrow();
        existing.setName(menu.getName());
        existing.setPrice(menu.getPrice());
        existing.setPrepTime(menu.getPrepTime());
        existing.setCategory(menu.getCategory());
        existing.setDescription(menu.getDescription());
        existing.setAvailable(menu.isAvailable());
        existing.setReadyMade(menu.isReadyMade());
        existing.setImageUrl(menu.getImageUrl());
        return repo.save(existing);
    }

    public void deleteItem(Long id) {
        repo.deleteById(id);
    }
}
