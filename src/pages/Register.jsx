import React, { useState, useEffect } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import NavBar from "../components/NavBar";

function Register() {
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [email, setEmail] = useState("");
	const [address, setAddress] = useState("");
	const [phone, setPhone] = useState("");
	const navigate = useNavigate();

	const api = "https://66fe49e22b9aac9c997b30ef.mockapi.io/User"; //API link goes here //This is just a mockAPI

	//Guide. https://www.youtube.com/embed/kvJLiKLOPtk?list=PL8p2I9GklV46469T9uxQ2ORe4AOzsqLb9
	async function handleRegister() {
		let user = { username, password, email, address, phone };
		console.warn(user);

		let result = await fetch(api, {
			method: "POST",
			body: JSON.stringify(user),
			headers: {
				"Content-Type": "application/json",
				Accept: "application/json",
			},
		});
		result = await result.json();
		console.warn(result);
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
					Register Form
				</Row>
				<Form className="col-sm-6 offset-sm-3">
					<Form.Group className="mb-3" controlId="formGroupUsername">
						<Form.Label>Username</Form.Label>
						<Form.Control type="text" placeholder="Username" required onChange={(e) => setUsername(e.target.value)} />
					</Form.Group>
					<Form.Group className="mb-3" controlId="formGroupPassword">
						<Form.Label>Password</Form.Label>
						<Form.Control type="password" placeholder="Password" required onChange={(e) => setPassword(e.target.value)} />
					</Form.Group>
					<Form.Group className="mb-3" controlId="formGroupEmail">
						<Form.Label>Email</Form.Label>
						<Form.Control type="text" placeholder="abcxyz@email.com" required onChange={(e) => setEmail(e.target.value)} />
					</Form.Group>
					<Form.Group className="mb-3" controlId="formGroupAddress">
						<Form.Label>Address</Form.Label>
						<Form.Control type="text" placeholder="Address" required onChange={(e) => setAddress(e.target.value)} />
					</Form.Group>
					<Form.Group className="mb-3" controlId="formGroupPhone">
						<Form.Label>Phone number</Form.Label>
						<Form.Control type="tel" placeholder="Phone number" required onChange={(e) => setPhone(e.target.value)} />
					</Form.Group>
					<Button onClick={handleRegister}>Register</Button>
				</Form>
				<Row>
					<Col>
						Already have an account?<Link to={"/Login"}>Login</Link> now.
					</Col>
				</Row>
			</Container>
		</>
	);
}

export default Register;
