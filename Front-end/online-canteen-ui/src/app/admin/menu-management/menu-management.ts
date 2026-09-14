import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../services/menu.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AdminNavbar } from '../admin-navbar/admin-navbar';

@Component({
  selector: 'app-menu-management',
  imports: [FormsModule, CommonModule, AdminNavbar],
  templateUrl: './menu-management.html',
  styleUrl: './menu-management.css',
})
export class MenuManagement implements OnInit {
  menuItems: any[] = [];
  // Newly Added - Store selected image
  selectedFile: File | null = null;
  form = {
    name: '',
    description: '',
    price: 0,
    prepTime: 0,
    imageUrl: '',
    category: '',
    available: true,
  };
  imagePreview: string | ArrayBuffer | null = null;
  constructor(private menuService: MenuService) {}

  ngOnInit(): void {
    this.loadMenu();
  }

  // Newly Added - Handle image selection
  // Newly Added - Handle image selection and preview
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files || input.files.length === 0) {
      return;
    }

    const file = input.files[0];

    const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];

    if (!allowedTypes.includes(file.type)) {
      alert('Only JPG, JPEG, PNG and WEBP images are allowed.');

      input.value = '';

      return;
    }

    const maxSize = 5 * 1024 * 1024;

    if (file.size > maxSize) {
      alert('Maximum image size is 5 MB.');

      input.value = '';

      return;
    }

    this.selectedFile = file;

    const reader = new FileReader();

    reader.onload = () => {
      this.imagePreview = reader.result;
    };

    reader.readAsDataURL(file);
  }

  loadMenu() {
    this.menuService.getAllMenuItems().subscribe({
      next: (res: any) => {
        this.menuItems = res;
      },
    });
  }

  // addItem() {
  //   this.menuService.addMenuItem(this.form).subscribe({
  //     next: () => {
  //       this.loadMenu();

  //       this.resetForm();
  //     },
  //   });
  // }

  addItem() {
    if (!this.selectedFile) {
      alert('Please select an image.');

      return;
    }

    const formData = new FormData();

    formData.append('file', this.selectedFile);

    formData.append('name', this.form.name);

    formData.append('price', this.form.price.toString());

    formData.append('prepTime', this.form.prepTime.toString());

    formData.append('available', this.form.available.toString());

    formData.append('readyMade', 'false');

    formData.append('description', this.form.description);

    formData.append('category', this.form.category);

    this.menuService.addMenuItem(formData).subscribe({
      next: () => {
        alert('Menu Item Added Successfully');

        this.loadMenu();

        this.resetForm();
      },

      error: (err) => {
        console.error(err);

        alert('Failed to add menu item');
      },
    });
  }

  deleteItem(id: number) {
    this.menuService.deleteMenuItem(id).subscribe({
      next: () => {
        this.loadMenu();
      },
    });
  }

  removeImage() {
    this.selectedFile = null;

    this.imagePreview = null;
  }

  // resetForm() {
  //   this.form = {
  //     name: '',
  //     description: '',
  //     price: 0,
  //     prepTime: 0,
  //     imageUrl: '',
  //     category: '',
  //     available: true,
  //   };
  // }

  resetForm() {
    this.form = {
      name: '',

      description: '',

      price: 0,

      prepTime: 0,

      imageUrl: '',

      category: '',

      available: true,
    };

    this.selectedFile = null;

    this.imagePreview = null;
  }
}