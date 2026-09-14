package com.OnlineCanteen.MenuService.repositrory;

import com.OnlineCanteen.MenuService.entity.Menu_Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu_Item,Long> {

}
