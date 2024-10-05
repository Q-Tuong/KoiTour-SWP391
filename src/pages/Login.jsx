import React, { useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { Link } from "react-router-dom";
import NavBar from "../components/NavBar";

function Login() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const api = ""; //api link goes here

	async function handleLogin() {}

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
								<Form.Control type="text" placeholder="Username" />
							</Form.Group>
							<Form.Group className="mb-3" controlId="formGroupPassword">
								<Form.Label>Password</Form.Label>
								<Form.Control type="password" placeholder="Password" />
							</Form.Group>
							<Button type="submit">Login</Button>
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
