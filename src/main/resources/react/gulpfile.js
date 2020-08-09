'use strict';

var gulp = require('gulp'),
    watch = require('gulp-watch'),
    prefixer = require('gulp-autoprefixer'),
    uglify = require('gulp-uglify'),
    sass = require('gulp-sass'),
    sourcemaps = require('gulp-sourcemaps'),
    rigger = require('gulp-rigger'),
    cssmin = require('gulp-clean-css'),
    imagemin = require('gulp-imagemin'),
    pngquant = require('imagemin-pngquant'),
    rimraf = require('rimraf'),
    browserSync = require("browser-sync"),
    reload = browserSync.reload;

    var path = {
        build: { 
          html: 'dist/',
          js: 'dist/js/',
          css: 'dist/css/',
          img: 'dist/img/MySettings/',
          fonts: 'dist/fonts/'
        },
        src: { 
          html: 'src/*.html', 
          js: 'src/js/main.js',
          style: 'src/scss/main.scss',
          img: 'src/img/MySettings/**/*.*', 
          fonts: 'src/fonts/**/*.*'
        },
        watch: { 
          html: 'src/**/*.html',
          js: 'src/js/**/*.js',
          style: 'src/scss/**/*.scss',
          img: 'src/img/MySettings/**/*.*',
          fonts: 'src/fonts/**/*.*'
        },
        clean: './dist'
  };

  var config = {
      server: {
          baseDir: "./dist"
      },
      tunnel: false,
      host: 'localhost',
      port: 9000,
      logPrefix: "Frontend"
};

gulp.task('html:build', async function () {
  gulp.src(path.src.html) 
      .pipe(rigger()) 
      .pipe(gulp.dest(path.build.html)) 
      .pipe(reload({stream: true})); 
});

gulp.task('js:build', async function () {
  gulp.src(path.src.js)
      .pipe(rigger())
      .pipe(sourcemaps.init()) 
      .pipe(uglify())
      .pipe(sourcemaps.write()) 
      .pipe(gulp.dest(path.build.js))
      .pipe(reload({stream: true})); 
});

gulp.task('style:build', async function () {
    gulp.src(path.src.style) 
        .pipe(sourcemaps.init()) 
        .pipe(sass().on('error', sass.logError)) 
        .pipe(prefixer('last 2 versions'))
        .pipe(cssmin()) 
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(path.build.css)) 
        .pipe(reload({stream: true}));
});

gulp.task('fonts:build', async function() {
    gulp.src(path.src.fonts)
        .pipe(gulp.dest(path.build.fonts))
});


//compressing all images

var cache = require('gulp-cache');
var imagemin = require('gulp-imagemin');
var imageminPngquant = require('imagemin-pngquant');
var imageminZopfli = require('imagemin-zopfli');
var imageminMozjpeg = require('imagemin-mozjpeg'); 
var imageminGiflossy = require('imagemin-giflossy');

gulp.task('image:build', async function() {
  return gulp.src(path.src.img)
      .pipe(cache(imagemin([
          //png
          imageminPngquant({
              speed: 1,
              quality: [0.95, 1] 
          }),
          imageminZopfli({
              more: true
          }),
         
          imageminGiflossy({
              optimizationLevel: 3,
              optimize: 3, 
              lossy: 2
          }),
         
          imagemin.svgo({
              plugins: [{
                  removeViewBox: false
              }]
          }),
          
          imagemin.jpegtran({
              progressive: true
          }),
          
          imageminMozjpeg({
              quality: 90
          })
      ])))
      .pipe(gulp.dest(path.build.img)); 
});

gulp.task('build', gulp.series( 
  'html:build',
  'js:build',
  'style:build',
  'fonts:build',
  'image:build'
));

gulp.task('watch', function(){
  watch([path.watch.html], function(event, cb) {
    gulp.parallel('html:build');
  });
});

gulp.task('watch', function(done){
  gulp.watch([path.watch.html], gulp.series('html:build')),
  gulp.watch([path.watch.style], gulp.series('style:build')),
  gulp.watch([path.watch.js], gulp.series('js:build')),
  gulp.watch([path.watch.img], gulp.series('image:build')),
  gulp.watch([path.watch.fonts], gulp.series('fonts:build')),
  done();
});

gulp.task('webserver', function () {
  browserSync(config);
});

gulp.task('clean', function (cb) {
  rimraf(path.clean, cb);
});

gulp.task('default', gulp.parallel('build', 'webserver', 'watch'));