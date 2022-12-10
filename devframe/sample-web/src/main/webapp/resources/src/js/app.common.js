import 'bootstrap';
import 'admin-lte';
import 'jquery-pjax';
import '@fortawesome/fontawesome-free/js/all';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'admin-lte/dist/css/adminlte.min.css';
import '../css/sample.css';

export default class Common {
	static init() {
		$(document).pjax('[data-pjax] a, a[data-pjax]', '#pjax-container');
	}
}
