import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MesOffres } from './mes-offres';

describe('MesOffres', () => {
  let component: MesOffres;
  let fixture: ComponentFixture<MesOffres>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MesOffres],
    }).compileComponents();

    fixture = TestBed.createComponent(MesOffres);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
