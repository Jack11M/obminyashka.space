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
        build: { //Тут мы укажем куда складывать готовые после сборки файлы
          html: 'dist/',
          js: 'dist/js/',
          css: 'dist/css/',
          img: 'dist/images/',
          fonts: 'dist/fonts/'
        },
        src: { //Пути откуда брать исходники
          html: 'src/*.html', //Синтаксис src/*.html говорит gulp что мы хотим взять все файлы с расширением .html
          js: 'src/js/*.js',//В стилях и скриптах нам понадобятся только main файлы
          // style: 'src/scss/main.scss',
          style: 'src/templates/all-style.scss',
          img: 'src/images/**/*.*', //Синтаксис img/**/*.* означает - взять все файлы всех расширений из папки и из вложенных каталогов
          fonts: 'src/fonts/**/*.*'
        },
        watch: { //Тут мы укажем, за изменением каких файлов мы хотим наблюдать
          html: 'src/**/*.html',
          js: 'src/js/**/*.js',          
          style: 'src/templates/Components/**/*.scss',          
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
      port: 3000,
      logPrefix: "Frontend"
};

gulp.task('html:build', async function () {
  gulp.src(path.src.html) //Выберем файлы по нужному пути
      .pipe(rigger()) //Прогоним через rigger
      .pipe(gulp.dest(path.build.html)) //Выплюнем их в папку build
      .pipe(reload({stream: true})); //И перезагрузим наш сервер для обновлений
});

gulp.task('js:build', async function () {
  gulp.src(path.src.js) //Найдем наш main файл
      .pipe(rigger()) //Прогоним через rigger
      .pipe(sourcemaps.init()) //Инициализируем sourcemap
      .pipe(uglify()) //Сожмем наш js
      .pipe(sourcemaps.write()) //Пропишем карты
      .pipe(gulp.dest(path.build.js)) //Выплюнем готовый файл в build
      .pipe(reload({stream: true})); //И перезагрузим сервер
});

gulp.task('style:build', async function () {
    gulp.src(path.src.style) //Выберем наш main.scss
        .pipe(sourcemaps.init()) //То же самое что и с js
        .pipe(sass().on('error', sass.logError)) //Скомпилируем
        .pipe(prefixer('last 2 versions')) //Добавим вендорные префиксы
        .pipe(cssmin()) //Сожмем
        .pipe(sourcemaps.write())
        .pipe(gulp.dest(path.build.css)) //И в build
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
var imageminMozjpeg = require('imagemin-mozjpeg'); //need to run 'brew install libpng'
var imageminGiflossy = require('imagemin-giflossy');

gulp.task('image:build', async function() {
  return gulp.src(path.src.img)
      .pipe(cache(imagemin([
          //png
          imageminPngquant({
              speed: 1,
              quality: [0.95, 1] //lossy settings
          }),
          imageminZopfli({
              more: true
              // iterations: 50 // very slow but more effective
          }),
          //gif
          // imagemin.gifsicle({
          //     interlaced: true,
          //     optimizationLevel: 3
          // }),
          //gif very light lossy, use only one of gifsicle or Giflossy
          imageminGiflossy({
              optimizationLevel: 3,
              optimize: 3, //keep-empty: Preserve empty transparent frames
              lossy: 2
          }),
          //svg
          imagemin.svgo({
              plugins: [{
                  removeViewBox: false
              }]
          }),
          //jpg lossless
          imagemin.jpegtran({
              progressive: true
          }),
          //jpg very light lossy, use vs jpegtran
          imageminMozjpeg({
              quality: 90
          })
      ])))
      .pipe(gulp.dest(path.build.img)); //И бросим в build
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