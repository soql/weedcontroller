import React from 'react';
import axios from 'axios';

class Logout extends React.Component {	

	componentWillMount(){
		 axios.get('logout').then(res => {
			 window.location="login.jsp";
		 });
	}
	
}
export default Logout;
