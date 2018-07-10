function newUser(){
	return {email: "", role: ""};
}

function clone(obj){
	return JSON.parse(JSON.stringify(obj));
}

function apiLogin(userName, password){
	let authText = "Basic " + btoa(userName + ":" + password);
	sessionStorage.setItem("api.authText", authText);
	return new Api(authText);
}

class Api{
	constructor(authText){
		this.authText = authText;
		
		this.onValidationError = this.newdefaultErrorHandler("ValidationError");
		this.onAuthenticationError = this.newdefaultErrorHandler("AuthenticationError");
		this.onAuthorisationError = this.newdefaultErrorHandler("AutorisationError");
		this.errorHandlers = {
			400 : () => this.onValidationError,
			401 : () => this.onAuthenticationError,
			403 : () => this.onAuthorisationError
		};
	}
	
	newdefaultErrorHandler(name){
		return (url, response) => console.error(`${name}: 
		url: ${url}
		response: ${response}`);
	}

	apiCall(url, method, data){
		var headers = new Headers();
		headers.append("Authorization", this.authText);
		headers.append("Content-Type", "application/json");
		var params = {
			headers : headers,
			method: method
		};
		if(data){
			params.body = JSON.stringify(data);
		}
		return fetch(url, params)
			.catch((error => {
				console.log("Network Error!");
				console.log(error);
			}))
			.then(response => {
				if(response.ok){
					return response.json();
				} else {
					let onError = this.errorHandlers[response.status]() || this.defaultErrorHandler;
					response.json().then(
						(jsonResponse) => onError(url, jsonResponse)
					);
				}
			});
	}
	
	apiGet(url){
		return this.apiCall(url, "GET");
	}
	
	apiPost(url, data){
		return this.apiCall(url, "POST", data);
	}
	
	apiDelete(url, data){
		return this.apiCall(url, "DELETE", data);
	}
}

var app = new Vue({
	el: '#app',
	data: {
		api: null,
		users: [],
		userRoles: [],
		loginView: {
			email: null,
			password: null
		},
		currentUserView: {
			user: newUser(),
			changePassword: false,
			newPassword: null,
			errorMessage: ""
		}
	},
	methods:{
		switchUser(user){
			this.currentUserView.changePassword = false;
			this.currentUserView.newPassword = "";
			this.currentUserView.errorMessage = "";
			this.currentUserView.user = clone(user);
		},
		newUser(){
			this.switchUser(newUser());
		},
		fetchUsers() {
			return this.api.apiGet("/users")
				.then(jsonUsers => this.users = jsonUsers);
		},
		fetchRoles(){
			return this.api.apiGet("/userRoles")
				.then(jsonRoles => this.userRoles = jsonRoles);
		},
		saveUser(){
			let userDto ={user: this.currentUserView.user};
			if(this.currentUserView.changePassword){
				userDto.password = this.currentUserView.newPassword;
			}
			return this.api.apiPost("/user", userDto)
				.then(jsonUser => {
					if(jsonUser){
						this.switchUser(jsonUser);
					}
					this.fetchUsers();
				});
		},
		deleteUser(user){
			this.api.apiDelete("/user/" + user.id)
				.then( () => {
					this.newUser();
					this.fetchUsers();
				});
		},
		setApi(api){
			this.api = api;
			this.api.onAuthenticationError = this.logout.bind(this);
			this.api.onAuthorisationError = () => this.currentUserView.errorMessage = "forbidden";
			this.api.onValidationError = (url, response) => {
				let firstField = Object.keys(response)[0];
				this.currentUserView.errorMessage = firstField + ": " + response[firstField];
			};
			this.newUser();
			this.loginView.password = "";
			this.fetchRoles();
			this.fetchUsers();
		},
		login(){
			let newApi = apiLogin(this.loginView.email, this.loginView.password);
			this.setApi(newApi);
		},
		logout(){
			sessionStorage.removeItem("api.authText");
			this.api = null;
		},
		tryToSetApiFromSession(){
			let authText = sessionStorage.getItem("api.authText");
			if(authText){
				this.setApi(new Api(authText));
			}
		}
	}
});

app.tryToSetApiFromSession(); 
