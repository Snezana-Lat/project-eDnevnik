package com.iktakademija.eDnevnik.security;



public class Views {

	public static class Public{}
	public static class Private extends Public{} // roditelj, ucenik, nastavnik
	public static class Admin extends Private{}
}
