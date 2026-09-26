import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreerOffre } from './creer-offre';

describe('CreerOffre', () => {
  let component: CreerOffre;
  let fixture: ComponentFixture<CreerOffre>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreerOffre],
    }).compileComponents();

    fixture = TestBed.createComponent(CreerOffre);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
