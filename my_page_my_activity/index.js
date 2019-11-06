import '/sass/custom.sass'

(function renderTemplates() {
    const elements = document.querySelectorAll('[data-template]');
    for (let i = 0; i < elements.length; i++) {
      const elm = elements[i];
      const templateId = elm.dataset.template;
      const template = document.querySelector(`#${templateId}`);
      elm.innerHTML = template.innerHTML;
      
    }
  })()