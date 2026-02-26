import { Component, Input, OnInit } from '@angular/core';
import { MovieService } from 'src/app/services/movie.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Movie } from 'src/app/models/movie.model';

@Component({
  selector: 'app-movie-details',
  templateUrl: './movie-details.component.html',
  styleUrls: ['./movie-details.component.css']
})
export class MovieDetailsComponent implements OnInit {

  @Input() viewMode = false;

  @Input() currentMovie: Movie = {
    title: '',
    director: '',
    watched: false
  };
  
  message = '';

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    if (!this.viewMode) {
      this.message = '';
      this.getMovie(this.route.snapshot.params["id"]);
    }
  }

  getMovie(id: string): void {
    this.movieService.get(id)
      .subscribe({
        next: (data) => {
          this.currentMovie = data;
          console.log(data);
        },
        error: (e) => console.error(e)
      });
  }

  updateWatched(status: boolean): void {
    const data = {
      title: this.currentMovie.title,
      director: this.currentMovie.director,
      watched: status
    };

    this.message = '';

    this.movieService.update(this.currentMovie.id, data)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.currentMovie.watched = status;
          this.message = res.message ? res.message : 'The status was updated successfully!';
        },
        error: (e) => console.error(e)
      });
  }

  updateMovie(): void {
    this.message = '';

    this.movieService.update(this.currentMovie.id, this.currentMovie)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.message = res.message ? res.message : 'This movie was updated successfully!';
        },
        error: (e) => console.error(e)
      });
  }

  deleteMovie(): void {
    this.movieService.delete(this.currentMovie.id)
      .subscribe({
        next: (res) => {
          console.log(res);
          this.router.navigate(['/movies']);
        },
        error: (e) => console.error(e)
      });
  }

}
