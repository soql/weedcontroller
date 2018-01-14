require('styles/App.css');

import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route, Switch} from 'react-router-dom'
import Main from './components/Main';
import ChartGenerator from './components/ChartGenerator';
import LiveView from './components/LiveView';
import Logout from './components/Logout';
import PowerUsage from './components/PowerUsage';
import GpioManager from './components/GpioManager';
import { Link, Match } from 'react-router-dom';
import { slide as Menu } from 'react-burger-menu'

ReactDOM.render((
	<BrowserRouter>
		<div>
			<Menu>  
				<Link className="menu-item" to="/">Główna</Link>
				<Link className="menu-item" to="wykresy">Wykresy</Link>
				<Link className="menu-item" to="live">Live</Link>
				<Link className="menu-item" to="powerUsage">Prąd</Link>
				<Link className="menu-item" to="nawilzacze">Nawilżacze</Link>
				<Link className="menu-item" to="ustawienia">Ustawienia</Link>
				<Link className="menu-item" to="/logout">Wyloguj</Link>				
			</Menu>
			<Switch>
				<Route exact path="/" component={Main}/>				
				<Route exact path="/wykresy" component={ChartGenerator} />
				<Route exact path="/live" component={LiveView} />
				<Route exact path="/powerUsage" component={PowerUsage} />
				<Route exact path="/logout" component={Logout} />
				<Route exact path="/nawilzacze" component={GpioManager} />
			</Switch>			
		</div>
	</BrowserRouter>
), document.getElementById('app'));

