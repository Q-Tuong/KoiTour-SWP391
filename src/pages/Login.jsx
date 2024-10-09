import React, { useEffect, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar";

function Login() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const navigate = useNavigate();

	const api = "https://66fe49e22b9aac9c997b30ef.mockapi.io/User"; //api link goes here

	async function handleLogin() {
		let user = { username, password };
		let result = await fetch(api, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				Accept: "application/json",
			},
			body: JSON.stringify(user),
		});
		result = await result.json();
		localStorage.setItem("user-info", JSON.stringify(result));
		navigate("/");
	}

	useEffect(() => {
		if (localStorage.getItem("user-info")) {
			navigate("/");
		}
	}, []);

	return (
		<>
			<NavBar />
			<Container>
				<Row as={"h2"} className="justify-content-center">
					Login Page
				</Row>
				<Form>
					<Row>
						<Col>
							<Link to={"#"}>Login with Facebook</Link>
							<br />
							<Link to={"#"}>Login with Twitter/X</Link>
							<br />
							<Link to={"#"}>Login with Google</Link>
							<br />
						</Col>
						<Col>
							<Form.Group className="mb-3" controlId="formGroupUsername">
								<Form.Label>Username</Form.Label>
								<Form.Control type="text" placeholder="Username" onChange={(e) => setUsername(e.target.value)} />
							</Form.Group>
							<Form.Group className="mb-3" controlId="formGroupPassword">
								<Form.Label>Password</Form.Label>
								<Form.Control type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
							</Form.Group>
							<Button onClick={handleLogin}>Login</Button>
						</Col>
					</Row>
				</Form>
				<Row>
					<Col>
						Don't have an account?<Link to={"/Register"}>Register</Link> now.
					</Col>
					<Col>
						<Link to={"/ForgotPassword"}>Forgot Password</Link>
					</Col>
				</Row>
			</Container>
		</>
	);
}

export default Login;
