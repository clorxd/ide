(function() {
	var PageEditor = Editor.Page;

	PageEditor.prototype.buildPageView = function($box) {
		this.layout_id_name = 'layout-id';
		this.layout_map = {};
		let root = this.getLayoutRoot();
		let id = this.getLayoutID(root);
		$box.attr(this.layout_id_name, id);
		this.layout_map[id] = root;
		let data = {};
		let $view = this.appendLayoutView($box, root, data);

		new Vue({
			el : $view[0],
			data : data,
			mounted () {}
		});
		this.$pageBox = $box;
		this.bindPageEvent($box);
	};

	PageEditor.prototype.appendLayoutView = function($parent, layout, data) {
		if (!layout) {
			return;
		}
		let $view = null;
		let id = this.getLayoutID(layout);
		this.layout_map[id] = layout;
		let template = this.getTemplateFromLayout(layout);
		if (template && coos.isNotEmpty(template.code)) {
			$view = $(template.code);
		} else {
			$view = $('<div ></div>');
		}
		$view.attr(this.layout_id_name, id);
		if (template) {
			if (template.isBlock) {
				$view.addClass('page-design-layout-block');
			}
		}

		$parent.append($view);
		if (layout.option) {
			let attr = layout.option.attr;
			if (attr) {
				Object.keys(attr).forEach(attrKey => {
					$view.attr(attrKey, attr[attrKey]);
				});
			}
			let slot = layout.option.slot;

			if (slot) {
				$view.append(slot);
			}
		}
		if (layout.layouts) {
			layout.layouts.forEach(one => {
				this.appendLayoutView($view, one);
			});
		}
		return $view;
	};

	PageEditor.prototype.bindPageEvent = function($box) {
		let that = this;

		$box.on('mouseover', function(e) {
			//let $layout = $(e.target).closest('[layout-id]');
			//$box.find('.page-design-layout-over').removeClass('page-design-layout-over');
			//$layout.addClass('page-design-layout-over');
		});
		$box.on('contextmenu', function(e) {
			e = e || window.event;

			that.onPageContextmenu(e);
			e.preventDefault();
		});
		$box.on('click', function(e) {
			e = e || window.event;
			that.onPageClick(e);
			e.preventDefault();
		});
		$box.on('scroll', function(e) {
			e = e || window.event;
			that.lastScrollTop = $box.scrollTop();
		});
		this.clickElByLayout(this.lastClickLayout);
		$box.scrollTop(that.lastScrollTop);
	};

	PageEditor.prototype.clickElByLayout = function(layout) {
		let id = this.getLayoutID(layout);
		let $el = this.$pageBox.find('[' + this.layout_id_name + '="' + id + '"]');
		layout = this.getLayoutFromEl($el);
		this.lastClickLayout = layout;
		let template = this.getTemplateFromLayout(layout);
		this.onSelectPageLayout(layout, template);
	};

	PageEditor.prototype.onPageClick = function(event) {

		this.onPageClickEl(event.target);

	};
	PageEditor.prototype.onPageClickEl = function(el) {

		let layout = this.getLayoutFromEl(el);
		this.lastClickLayout = layout;
		if (layout == null) {
			return;
		}
		let template = this.getTemplateFromLayout(layout);
		this.onSelectPageLayout(layout, template);

	};
	PageEditor.prototype.onPageContextmenu = function(event) {
		let that = this;
		var eventData = {
			clientX : event.clientX,
			clientY : event.clientY
		};
		var menus = [];


		let layout = this.getLayoutFromEvent(event);
		if (layout == null) {
			return;
		}
		let parentLayout = this.getLayoutParent(layout);

		menus.push({
			text : "添加",
			onClick : function() {
				that.addPageLayoutChild(layout);
			}
		});
		if (parentLayout) {
			menus.push({
				text : "前边添加",
				onClick : function() {
					that.addPageLayoutBefore(layout, parentLayout);
				}
			});
			menus.push({
				text : "后边添加",
				onClick : function() {
					that.addPageLayoutAfter(layout, parentLayout);
				}
			});
			menus.push({
				text : "上移",
				onClick : function() {
					that.moveUpPageLayout(layout, parentLayout);
				}
			});
			menus.push({
				text : "下移",
				onClick : function() {
					that.moveDwPageLayout(layout, parentLayout);
				}
			});
			menus.push({
				text : "删除",
				onClick : function() {
					that.removePageLayout(layout, parentLayout);
				}
			});
		}

		source.repository.contextmenu.menus = menus;
		source.repository.contextmenu.callShow(event);
	};
})();