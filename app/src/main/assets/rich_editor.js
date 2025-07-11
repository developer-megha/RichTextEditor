/**
 * Copyright (C) 2020 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * See about document.execCommand: https://developer.mozilla.org/en-US/docs/Web/API/Document/execCommand
 */

var RE = {};

RE.currentSelection = {
    "startContainer": 0,
    "startOffset": 0,
    "endContainer": 0,
    "endOffset": 0};

RE.editor = document.getElementById('editor');

document.addEventListener("selectionchange", function() { RE.backuprange(); });

// Initializations
RE.callback = function() {
    window.location.href = "re-callback://" + encodeURIComponent(RE.getHtml());
}

RE.setHtml = function(contents) {
    RE.editor.innerHTML = decodeURIComponent(contents.replace(/\+/g, '%20'));
}

//RE.getHtml = function() {
//    return RE.editor.innerHTML;
//}

RE.getHtml = function () {
    const clone = RE.editor.cloneNode(true);

    // Clean font-size visual boost and remove data-font-size
    const spans = clone.querySelectorAll("span[data-font-size]");
    spans.forEach(span => {
        const realSize = span.getAttribute("data-font-size");
        span.style.fontSize = realSize;
        span.removeAttribute("data-font-size");
    });

    let html = clone.innerHTML;

    // Normalize &quot; to single quotes
    html = html.replace(/font-family:\s*&quot;([^"]*)&quot;/g, "font-family: '$1'");

    // Wrap font-family values in single quotes if not already quoted
    html = html.replace(/font-family:\s*([^;'""]+);/g, "font-family: '$1';");

    return html;
};

RE.getText = function() {
    return RE.editor.innerText;
}

RE.setBaseTextColor = function(color) {
    RE.editor.style.color  = color;
}

RE.setBaseFontSize = function(size) {
    RE.editor.style.fontSize = size;
}

RE.applyFontSizeTag = function(htmlSize, visualSize) {
    RE.restorerange();

    const sel = window.getSelection();
    if (!sel.rangeCount || sel.isCollapsed) return;

    const range = sel.getRangeAt(0);
    const content = range.extractContents();

    const container = document.createElement("div");
    container.appendChild(content);

    // Remove old span/font-size wrappers
    const spans = container.querySelectorAll("span");
    spans.forEach(span => {
        span.style.fontSize = null;
        if (span.getAttribute("style") === "") span.removeAttribute("style");

        const parent = span.parentNode;
        while (span.firstChild) parent.insertBefore(span.firstChild, span);
        parent.removeChild(span);
    });

    // Apply new span with both visual and saved sizes
    const span = document.createElement("span");
    span.style.fontSize = visualSize; // Visual size for editor
    span.setAttribute("data-font-size", htmlSize); // Store actual size for export

    while (container.firstChild) {
        span.appendChild(container.firstChild);
    }

    range.insertNode(span);
    RE.callback();
};

RE.getCleanHtml = function() {
    const clone = RE.editor.cloneNode(true);

    const spans = clone.querySelectorAll("span[data-font-size]");
    spans.forEach(span => {
        const realSize = span.getAttribute("data-font-size");
        span.style.fontSize = realSize;
        span.removeAttribute("data-font-size");
    });

    return clone.innerHTML;
};

RE.setFontFamily = function(fontName) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand("fontName", false, fontName);
    document.execCommand("styleWithCSS", null, false);
}

RE.setPadding = function(left, top, right, bottom) {
  RE.editor.style.paddingLeft = left;
  RE.editor.style.paddingTop = top;
  RE.editor.style.paddingRight = right;
  RE.editor.style.paddingBottom = bottom;
}

RE.setBackgroundColor = function(color) {
    document.body.style.backgroundColor = color;
}

RE.setBackgroundImage = function(image) {
    RE.editor.style.backgroundImage = image;
}

RE.setWidth = function(size) {
    RE.editor.style.minWidth = size;
}

RE.setHeight = function(size) {
    RE.editor.style.height = size;
}

RE.setTextAlign = function(align) {
    RE.editor.style.textAlign = align;
}

RE.setVerticalAlign = function(align) {
    RE.editor.style.verticalAlign = align;
}

RE.setPlaceholder = function(placeholder) {
    RE.editor.setAttribute("placeholder", placeholder);
}

RE.setInputEnabled = function(inputEnabled) {
    RE.editor.contentEditable = String(inputEnabled);
}

RE.undo = function() {
    document.execCommand('undo', false, null);
}

RE.redo = function() {
    document.execCommand('redo', false, null);
}

RE.setBold = function() {
    document.execCommand('bold', false, null);
}

RE.setItalic = function() {
    document.execCommand('italic', false, null);
}

RE.setSubscript = function() {
    document.execCommand('subscript', false, null);
}

RE.setSuperscript = function() {
    document.execCommand('superscript', false, null);
}

RE.setStrikeThrough = function() {
    document.execCommand('strikeThrough', false, null);
}

RE.setUnderline = function() {
    document.execCommand('underline', false, null);
}

RE.setBullets = function() {
    document.execCommand('insertUnorderedList', false, null);
}

RE.setNumbers = function() {
    document.execCommand('insertOrderedList', false, null);
}

RE.setTextColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('foreColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

RE.setTextBackgroundColor = function(color) {
    RE.restorerange();
    document.execCommand("styleWithCSS", null, true);
    document.execCommand('hiliteColor', false, color);
    document.execCommand("styleWithCSS", null, false);
}

//RE.setFontSize = function(fontSize){
//    document.execCommand("fontSize", false, fontSize);
//}

RE.setHeading = function(heading) {
    document.execCommand('formatBlock', false, '<h'+heading+'>');
}

RE.setIndent = function() {
    document.execCommand('indent', false, null);
}

RE.setOutdent = function() {
    document.execCommand('outdent', false, null);
}

RE.setJustifyLeft = function() {
    document.execCommand('justifyLeft', false, null);
}

RE.setJustifyCenter = function() {
    document.execCommand('justifyCenter', false, null);
}

RE.setJustifyRight = function() {
    document.execCommand('justifyRight', false, null);
}

RE.setJustifyFull = function() {
    document.execCommand('justifyFull', false, null);
}

RE.setBlockquote = function() {
    document.execCommand('formatBlock', false, '<blockquote>');
}

RE.insertImage = function(url, alt) {
    var html = '<img src="' + url + '" alt="' + alt + '" />';
    RE.insertHTML(html);
}

RE.insertImageW = function(url, alt, width) {
    var html = '<img src="' + url + '" alt="' + alt + '" width="' + width + '"/>';
    RE.insertHTML(html);
}

RE.insertImageWH = function(url, alt, width, height) {
    var html = '<img src="' + url + '" alt="' + alt + '" width="' + width + '" height="' + height +'"/>';
    RE.insertHTML(html);
}

RE.insertVideo = function(url, alt) {
    var html = '<video src="' + url + '" controls></video><br>';
    RE.insertHTML(html);
}

RE.insertVideoW = function(url, width) {
    var html = '<video src="' + url + '" width="' + width + '" controls></video><br>';
    RE.insertHTML(html);
}

RE.insertVideoWH = function(url, width, height) {
    var html = '<video src="' + url + '" width="' + width + '" height="' + height + '" controls></video><br>';
    RE.insertHTML(html);
}

RE.insertAudio = function(url, alt) {
    var html = '<audio src="' + url + '" controls></audio><br>';
    RE.insertHTML(html);
}

RE.insertYoutubeVideo = function(url) {
    var html = '<iframe width="100%" height="100%" src="' + url + '" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe><br>'
    RE.insertHTML(html);
}

RE.insertYoutubeVideoW = function(url, width) {
    var html = '<iframe width="' + width + '" src="' + url + '" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe><br>'
    RE.insertHTML(html);
}

RE.insertYoutubeVideoWH = function(url, width, height) {
    var html = '<iframe width="' + width + '" height="' + height + '" src="' + url + '" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe><br>'
    RE.insertHTML(html);
}

RE.insertHTML = function(html) {
    RE.restorerange();
    document.execCommand('insertHTML', false, html);
}

RE.insertLink = function(url) {
    RE.restorerange();

    var sel = document.getSelection();

    // Do nothing if no text is selected
    if (!sel || sel.isCollapsed || sel.toString().trim() === "") {
        return;
    }

    // Create anchor and apply href
    if (sel.rangeCount) {
        var el = document.createElement("a");
        el.setAttribute("href", url);
        el.setAttribute("target", "_blank"); // optional: open in new tab

        var range = sel.getRangeAt(0).cloneRange();
        range.surroundContents(el);
        sel.removeAllRanges();
        sel.addRange(range);
    }

    RE.callback();
}

RE.editor.addEventListener('click', function(event) {
    const target = event.target;
    if (target.tagName === 'A') {
        event.preventDefault();
        var url = target.getAttribute('href');
        Android.onLinkClicked(url);
    }
});

RE.setTodo = function(text) {
    var html = '<input type="checkbox" name="'+ text +'" value="'+ text +'"/> &nbsp;';
    document.execCommand('insertHTML', false, html);
}

RE.prepareInsert = function() {
    RE.backuprange();
}

RE.backuprange = function(){
    var selection = window.getSelection();
    if (selection.rangeCount > 0) {
      var range = selection.getRangeAt(0);
      RE.currentSelection = {
          "startContainer": range.startContainer,
          "startOffset": range.startOffset,
          "endContainer": range.endContainer,
          "endOffset": range.endOffset};
    }
}

RE.restorerange = function(){
    var selection = window.getSelection();
    selection.removeAllRanges();
    var range = document.createRange();
    range.setStart(RE.currentSelection.startContainer, RE.currentSelection.startOffset);
    range.setEnd(RE.currentSelection.endContainer, RE.currentSelection.endOffset);
    selection.addRange(range);
}

RE.enabledEditingItems = function(e) {
    var items = [];
    if (document.queryCommandState('bold')) {
        items.push('bold');
    }
    if (document.queryCommandState('italic')) {
        items.push('italic');
    }
    if (document.queryCommandState('subscript')) {
        items.push('subscript');
    }
    if (document.queryCommandState('superscript')) {
        items.push('superscript');
    }
    if (document.queryCommandState('strikeThrough')) {
        items.push('strikeThrough');
    }
    if (document.queryCommandState('underline')) {
        items.push('underline');
    }
    if (document.queryCommandState('insertOrderedList')) {
        items.push('orderedList');
    }
    if (document.queryCommandState('insertUnorderedList')) {
        items.push('unorderedList');
    }
    if (document.queryCommandState('justifyCenter')) {
        items.push('justifyCenter');
    }
    if (document.queryCommandState('justifyFull')) {
        items.push('justifyFull');
    }
    if (document.queryCommandState('justifyLeft')) {
        items.push('justifyLeft');
    }
    if (document.queryCommandState('justifyRight')) {
        items.push('justifyRight');
    }
    if (document.queryCommandState('insertHorizontalRule')) {
        items.push('horizontalRule');
    }
    var formatBlock = document.queryCommandValue('formatBlock');
    if (formatBlock.length > 0) {
        items.push(formatBlock);
    }

    window.location.href = "re-state://" + encodeURI(items.join(','));
}

RE.focus = function() {
    var range = document.createRange();
    range.selectNodeContents(RE.editor);
    range.collapse(false);
    var selection = window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
    RE.editor.focus();
}

RE.blurFocus = function() {
    RE.editor.blur();
}

RE.removeFormat = function() {
    document.execCommand('removeFormat', false, null);
    document.execCommand('unlink', false, null); // also remove links
}

RE.clearFormatting = function() {
     RE.restorerange();

     var selection = window.getSelection();

     if (selection && selection.rangeCount > 0 && !selection.isCollapsed) {
         // Case 1: text is selected — remove formatting from selected text
         document.execCommand('removeFormat', false, null);
         document.execCommand('unlink', false, null);
     } else {
         // Case 2: no selection — just reset formatting for future typing
         document.execCommand('removeFormat', false, null);
         document.execCommand('unlink', false, null);

         // additionally, reset font, size, color, etc. to default
         document.execCommand("styleWithCSS", null, true);
         document.execCommand("foreColor", false, "#000000");
         document.execCommand("fontName", false, "sans-serif");
         document.execCommand("fontSize", false, "3"); // Default font size
         document.execCommand("styleWithCSS", null, false);
     }

     RE.callback();
}

// Event Listeners
RE.editor.addEventListener("input", RE.callback);
RE.editor.addEventListener("keyup", function(e) {
    var KEY_LEFT = 37, KEY_RIGHT = 39;
    if (e.which == KEY_LEFT || e.which == KEY_RIGHT) {
        RE.enabledEditingItems(e);
    }
});
RE.editor.addEventListener("click", RE.enabledEditingItems);
