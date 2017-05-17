import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Switch} from 'react-router-dom'
import Main from './components/Main';
import ChartGenerator from './components/ChartGenerator';
import { Link, Match } from 'react-router-dom';
import { slide as Menu } from 'react-burger-menu'

ReactDOM.render((
	<BrowserRouter>
		<div>
			<Menu>  
				<Link className="menu-item" to="/">Główna</Link>
				<Link className="menu-item" to="wykresy">Wykresy</Link>
				<Link className="menu-item" to="ustawienia">Ustawienia</Link>
			</Menu>
			<Switch>
				<Route exact path="/" component={Main}/>
				<Route exact path="/wykresy" component={ChartGenerator} />
			</Switch>			
		</div>
	</BrowserRouter>
), document.getElementById('app'));

