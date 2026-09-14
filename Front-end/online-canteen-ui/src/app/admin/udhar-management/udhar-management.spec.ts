import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UdharManagement } from './udhar-management';

describe('UdharManagement', () => {
  let component: UdharManagement;
  let fixture: ComponentFixture<UdharManagement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UdharManagement],
    }).compileComponents();

    fixture = TestBed.createComponent(UdharManagement);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
