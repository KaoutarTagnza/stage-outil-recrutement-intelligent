import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListeOffres } from './liste-offres';

describe('ListeOffres', () => {
  let component: ListeOffres;
  let fixture: ComponentFixture<ListeOffres>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListeOffres],
    }).compileComponents();

    fixture = TestBed.createComponent(ListeOffres);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
