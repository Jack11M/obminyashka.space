let count = 0;
let imageCount = document.getElementById('image-count');

function previewFile(id, label) {
    let preview = document.getElementById(id);
    let file = document.getElementById(label).files[0];
    let reader = new FileReader();
    reader.addEventListener("load", function () {
        preview.style.cssText = `background-image: url(${reader.result});`;
    }, false);

    if (file) {
        reader.readAsDataURL(file);
    }
}

document.getElementById('label-one').onchange = function () {
    previewFile('first-preview', 'label-one');
    let del = document.createElement('button');
    del.setAttribute('class', 'del-button');
    if (this.files.length > 0) {
        document.getElementById('first-preview').appendChild(del);
        document.getElementById('first-preview').children[0].style.display = 'none';
        count++;
        imageCount.innerText = `Загружено фотографий ${count} из 6`
        del.addEventListener('click', function () {
            document.getElementById('first-preview').removeChild(del);
            document.getElementById('first-preview').children[0].style.display = 'block';
            document.getElementById('first-preview').style.background = 'transparent';
            count--;
            imageCount.innerText = `Загружено фотографий ${count} из 6`
        })
    }
};
document.getElementById('label-two').onchange = function () {
    previewFile('second-preview', 'label-two');
    let del = document.createElement('button');
    del.setAttribute('class', 'del-button');
    if (this.files.length > 0) {
        document.getElementById('second-preview').appendChild(del);
        document.getElementById('second-preview').children[0].style.display = 'none';
        count++;
        imageCount.innerText = `Загружено фотографий ${count} из 6`
        del.addEventListener('click', function () {
            document.getElementById('second-preview').removeChild(del);
            document.getElementById('second-preview').children[0].style.display = 'block';
            document.getElementById('second-preview').style.background = 'transparent';
            count--;
            imageCount.innerText = `Загружено фотографий ${count} из 6`
        })
    }
};
document.getElementById('label-three').onchange = function () {
    previewFile('third-preview', 'label-three');
    let del = document.createElement('button');
    del.setAttribute('class', 'del-button');
    if (this.files.length > 0) {
        document.getElementById('third-preview').appendChild(del);
        document.getElementById('third-preview').children[0].style.display = 'none';
        count++;
        imageCount.innerText = `Загружено фотографий ${count} из 6`
        del.addEventListener('click', function () {
            document.getElementById('third-preview').removeChild(del);
            document.getElementById('third-preview').children[0].style.display = 'block';
            document.getElementById('third-preview').style.background = 'transparent';
            count--;
            imageCount.innerText = `Загружено фотографий ${count} из 6`
        })
    }
};
document.getElementById('label-four').onchange = function () {
    previewFile('fourth-preview', 'label-four');
    let del = document.createElement('button');
    del.setAttribute('class', 'del-button');
    if (this.files.length > 0) {
        document.getElementById('fourth-preview').appendChild(del);
        document.getElementById('fourth-preview').children[0].style.display = 'none';
        count++;
        imageCount.innerText = `Загружено фотографий ${count} из 6`
        del.addEventListener('click', function () {
            document.getElementById('fourth-preview').removeChild(del);
            document.getElementById('fourth-preview').children[0].style.display = 'block';
            document.getElementById('fourth-preview').style.background = 'transparent';
            count--;
            imageCount.innerText = `Загружено фотографий ${count} из 6`
        })
    }
};
document.getElementById('label-five').onchange = function () {
    previewFile('fifth-preview', 'label-five');
    let del = document.createElement('button');
    del.setAttribute('class', 'del-button');
    if (this.files.length > 0) {
        document.getElementById('fifth-preview').appendChild(del);
        document.getElementById('fifth-preview').children[0].style.display = 'none';
        count++;
        imageCount.innerText = `Загружено фотографий ${count} из 6`
        del.addEventListener('click', function () {
            document.getElementById('fifth-preview').removeChild(del);
            document.getElementById('fifth-preview').children[0].style.display = 'block';
            document.getElementById('fifth-preview').style.background = 'transparent';
            count--;
            imageCount.innerText = `Загружено фотографий ${count} из 6`
        })
    }
};
document.getElementById('label-six').onchange = function () {
    previewFile('sixth-preview', 'label-six');
    let del = document.createElement('button');
    del.setAttribute('class', 'del-button');
    if (this.files.length > 0) {
        document.getElementById('sixth-preview').appendChild(del);
        document.getElementById('sixth-preview').children[0].style.display = 'none';
        count++;
        imageCount.innerText = `Загружено фотографий ${count} из 6`
        del.addEventListener('click', function () {
            document.getElementById('sixth-preview').removeChild(del);
            document.getElementById('sixth-preview').children[0].style.display = 'block';
            document.getElementById('sixth-preview').style.background = 'transparent';
            count--;
            imageCount.innerText = `Загружено фотографий ${count} из 6`
        })
    }
};