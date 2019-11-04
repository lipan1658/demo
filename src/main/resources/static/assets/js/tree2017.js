;
(function ($, window, document, undefined) {

	'use strict';

	var pluginName = 'tree2017';

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
	<li><a href="https://adminlte.io/docs"><i class="fa fa-book"></i> <span>Documentation</span></a></li>
	<li class="treeview">
		<a href="#">
			<i class="fa fa-share"></i> <span>Multilevel</span>
			<span class="pull-right-container">
			  <i class="fa fa-angle-left pull-right"></i>
			</span>
		</a>
		<ul class="treeview-menu">
			<li><a href="#"><i class="fa fa-circle-o"></i> Level One</a></li>
			<li class="treeview">
				<a href="#"><i class="fa fa-circle-o"></i> Level One
					<span class="pull-right-container">
					  <i class="fa fa-angle-left pull-right"></i>
					</span>
				</a>
				<ul class="treeview-menu">
					<li><a href="#"><i class="fa fa-circle-o"></i> Level Two</a></li>
					<li class="treeview">
						<a href="#"><i class="fa fa-circle-o"></i> Level Two
							<span class="pull-right-container">
							  <i class="fa fa-angle-left pull-right"></i>
							</span>
						</a>
						<ul class="treeview-menu">
							<li><a href="#"><i class="fa fa-circle-o"></i> Level Three</a></li>
							<li><a href="#"><i class="fa fa-circle-o"></i> Level Three</a></li>
						</ul>
					</li>
				</ul>
			</li>
			<li><a href="#"><i class="fa fa-circle-o"></i> Level One</a></li>
		</ul>
	</li>*/
	//结构模板
	BaseTree.prototype.template = {
		li_treeview: '<li class="treeview"></li>',
		li_treeview_a: '<a id="menu{0}"  href="#" data-url="{1}"><i class = "fa {2}"></i><span>{3}</span><span class = ' +
			'"pull-right-container"><i class = "fa fa-angle-left pull-right"> </i></span ></a>',
		li_ul: '<ul class="treeview-menu"></ul>',
		li: '<li><a id="menu{0}" href="#" data-url="{1}"><i class="fa {2}"></i> <span>{3}</span></a></li>'
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
			if (node.nodes.length == 0) {
				var li = $(template.li.format(node.id, baseUrl+node.href, node.icon == undefined ? "fa fa-share" : node.icon, node.text));
				ele.append(li);
			} else {
				var li_treeview = $(template.li_treeview);
				var li_treeview_a = $(template.li_treeview_a.format(node.id, baseUrl+node.href, node.icon == undefined ? "fa fa-share" : node.icon, node.text));
				var li_ul = $(template.li_ul);
				li_ul = me.parse(li_ul, node.nodes,baseUrl)

				li_treeview.append(li_treeview_a);
				li_treeview.append(li_ul);
				ele.append(li_treeview);
			}
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
