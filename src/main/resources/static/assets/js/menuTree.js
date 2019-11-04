;
(function ($, window, document, undefined) {

	'use strict';

	var pluginName = 'menuTree';

	//入口方法
	$.fn[pluginName] = function (options) {
		var self = $(this);
		if (this == null)
			return null;
		var data = this.data(pluginName);
		if (!data) {
			data = new BaseTree(this, options);
			self.data(pluginName, data);
		}
		return data;
	};

	var BaseTree = function (element, options) {
		this.$element = $(element);
		this.options = $.extend(true, {}, this.default, options);
		this.init();
	}

	/*
      <li class=" nav-item"><a href="#"><i class="icon-android-funnel"></i><span data-i18n="nav.menu_levels.main" class="menu-title">Menu levels</span></a>
        <ul class="menu-content">

          </li>
          <li><a href="#" data-i18n="nav.menu_levels.second_level_child.main" class="menu-item">Second level child</a>
            <ul class="menu-content">
              <li><a href="#" data-i18n="nav.menu_levels.second_level_child.third_level" class="menu-item">Third level</a>
              </li>
              <li><a href="#" data-i18n="nav.menu_levels.second_level_child.third_level_child.main" class="menu-item">Third level child</a>
                <ul class="menu-content">
                  <li><a href="#" data-i18n="nav.menu_levels.second_level_child.third_level_child.fourth_level1" class="menu-item">Fourth level</a>
                  </li>
                  <li><a href="#" data-i18n="nav.menu_levels.second_level_child.third_level_child.fourth_level2" class="menu-item">Fourth level</a>
                  </li>
                </ul>
              </li>
            </ul>
          </li>
        </ul>
      </li>
	*/
	//结构模板
	BaseTree.prototype.template = {
		nav_item:'<li class="nav-item"></li>',
		nav_item_p:'<li class="nav-item has-sub"></li>',
		menu_title:'<a href="#" data-url="{0}"><i class="{1}"></i><span data-i18n="nav.menu_levels.main" class="menu-title">{2}</span></a>',
		menu_content:'<ul class="menu-content">',
		menu_item:'<li><a href="#" data-url="{0}" data-i18n="nav.menu_levels.second_level" class="menu-item">{1}</a>',
		menu_item_p:'<li class="has-sub"><a href="#" data-url="{0}" data-i18n="nav.menu_levels.second_level" class="menu-item">{1}</a>',
	}

	//初始化
	BaseTree.prototype.init = function () {
		if (!this.options.data || this.options.data.length == 0) {
			console.error("无数据");
			return;
		}

		//清除原来的tab页
		this.$element.html("");
		this.builder(this.options);
	}


	//使用模板搭建页面结构
	BaseTree.prototype.builder = function (opts) {
		this.parse(this.$element, opts.data,opts.baseUrl);

	}

	BaseTree.prototype.parse = function (ele, data, baseUrl) {
		var template = this.template;
		var me = this;

		$.each(data, function (i, node) {
			//根节点
			if(node.pid==1000){
				if(node.menus.length == 0){
					var nav_item = $(template.nav_item);
					var menu_title = $(template.menu_title.format(baseUrl+node.url,node.iconCls == undefined ? "icon-android-funnel":node.iconCls,node.menuName));
					nav_item.append(menu_title);
					ele.append(nav_item);
				}else{
					var nav_item_p = $(template.nav_item_p);
					var menu_title = $(template.menu_title.format(baseUrl+node.url,node.iconCls == undefined ? "icon-android-funnel":node.iconCls,node.menuName));
					var menu_content = $(template.menu_content);
					menu_content = me.parse(menu_content, node.menus,baseUrl)
					
					nav_item_p.append(menu_title);
					nav_item_p.append(menu_content);
					ele.append(nav_item_p);
						
				}
			}else{
				if(node.menus.length == 0){
					var menu_item = $(template.menu_item.format(baseUrl+node.url,node.menuName));
					ele.append(menu_item);
				}else{
					var menu_item_p = $(template.menu_item_p.format(baseUrl+node.url,node.menuName));
					var menu_content = $(template.menu_content);
					menu_content = me.parse(menu_content, node.menus,baseUrl)
					
					menu_item_p.append(menu_content);
					ele.append(menu_item_p);
						
				}
			}
			
			
			
//			if (node.nodes.length == 0) {
//				var menu_item = $(template.menu_item);
//				ele.append(menu_item);
//			} else {
//				var nav_item_p = $(template.nav_item_p);
//				if(node.icon==''){
//					var menu_title = $(template.menu_title);
//					var menu_content = $(template.menu_content);
//					menu_content = me.parse(menu_content, node.nodes,baseUrl)
//					
//					nav_item_p.append(menu_title);
//					nav_item_p.append(menu_content);
//					ele.append(nav_item_p);
//				}else{
//					var menu_title = $(template.menu_title);
//					var menu_content = $(template.menu_content);
//					menu_content = me.parse(menu_content, node.nodes,baseUrl)
//					
//					nav_item_p.append(menu_title);
//					nav_item_p.append(menu_content);
//					ele.append(nav_item_p);
//					
//				}
//			}
		});
		return ele;
	}

	String.prototype.format = function () {
		if (arguments.length == 0) return this;
		for (var s = this, i = 0; i < arguments.length; i++)
			s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
		return s;
	};


})(jQuery, window, document)
