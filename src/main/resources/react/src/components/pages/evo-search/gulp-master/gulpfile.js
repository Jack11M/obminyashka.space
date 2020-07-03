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
          img: 'dist/images/',
          fonts: 'dist/fonts/'
        },
        src: { //Ways to get sources from
          html: 'src/*.html', 
          js: 'src/js/main.js',//In styles and scripts we need only main files
          style: 'src/scss/main.scss',
          img: 'src/images/**/*.*', //The syntax img /**/*.* means - take all files of all extensions from the folder and from the subdirectories
          fonts: 'src/fonts/**/*.*'
        },
        watch: { //Here we indicate which files we want to monitor.
          html: 'src/**/*.html',
          js: 'src/js/**/*.js',
          style: 'src/scss/**/*.scss',
          img: 'src/images/**/*.*',
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
  gulp.src(path.src.html) //Select the files in the desired path
      .pipe(rigger()) //Drive through rigger
      .pipe(gulp.dest(path.build.html)) //Spit them out in the build folder
      .pipe(reload({stream: true})); //And restart our server for updates
});

gulp.task('js:build', async function () {
  gulp.src(path.src.js) //Find our main file
      .pipe(rigger()) // Drive through rigger
      .pipe(sourcemaps.init()) //Initialize sourcemap
      .pipe(uglify()) //Squeeze our js
      .pipe(sourcemaps.write()) //We will write cards
      .pipe(gulp.dest(path.build.js)) //Spit out the finished file in build
      .pipe(reload({stream: true})); //And restart the server
});

gulp.task('style:build', async function () {
    gulp.src(path.src.style) //Choose our main.scss
        .pipe(sourcemaps.init()) //Same as js
        .pipe(sass().on('error', sass.logError)) //Compile
        .pipe(prefixer('last 2 versions')) //Add vendor prefixes
        .pipe(cssmin()) //Squeeze
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(path.build.css)) // And in build
        .pipe(reload({stream: true}));
});

gulp.task('fonts:build', async function() {
    gulp.src(path.src.fonts)
        .pipe(gulp.dest(path.build.fonts))
});

var cache = require('gulp-cache');
var imagemin = require('gulp-imagemin');
var imageminPngquant = require('imagemin-pngquant');
var imageminZopfli = require('imagemin-zopfli');
var imageminMozjpeg = require('imagemin-mozjpeg'); 
var imageminGiflossy = require('imagemin-giflossy');

gulp.task('image:build', async function() {
  return gulp.src(path.src.img)
      .pipe(cache(imagemin([
          
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