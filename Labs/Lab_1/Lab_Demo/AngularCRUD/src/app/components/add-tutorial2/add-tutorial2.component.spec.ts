import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTutorial2Component } from './add-tutorial2.component';

describe('AddTutorial2Component', () => {
  let component: AddTutorial2Component;
  let fixture: ComponentFixture<AddTutorial2Component>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddTutorial2Component ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddTutorial2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
